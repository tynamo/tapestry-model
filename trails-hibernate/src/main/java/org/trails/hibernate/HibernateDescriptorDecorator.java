/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.hibernate;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Selectable;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.trails.TrailsRuntimeException;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.DescriptorDecorator;
import org.trails.descriptor.DescriptorFactory;
import org.trails.descriptor.EmbeddedDescriptor;
import org.trails.descriptor.EnumReferenceDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.ObjectReferenceDescriptor;
import org.trails.descriptor.OwningObjectReferenceDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;

/**
 * This decorator will add metadata information. It will replace simple
 * reflection based TrailsPropertyIPropertyDescriptors with appropriate
 * Hibernate descriptors <p/> Background... TrailsDescriptorService operates one
 * ReflectorDescriptorFactory - TrailsDescriptorService iterates/scans all class
 * types encountered - ReflectorDescriptorFactory allocates property descriptor
 * instance for the class type - TrailsDescriptorService decorates property
 * descriptor by calling this module HibernateDescriptorDecorator -
 * HibernateDescriptorDecorator caches the decorated property descriptor into a
 * decorated descriptor list - decorated descriptor list gets populated into
 * class descriptor for class type - TrailsDescriptorService finally populates
 * decorated class descriptor and it's aggregated list of decorated property
 * descriptors into it's own list/cache of referenced class descriptors
 * 
 * @see TrailsPropertyDescriptor
 * @see ObjectReferenceDescriptor
 * @see OwningObjectReferenceDescriptor
 * @see CollectionDescriptor
 * @see EmbeddedDescriptor
 */
public class HibernateDescriptorDecorator implements DescriptorDecorator
{
	protected static final Log LOG = LogFactory.getLog(HibernateDescriptorDecorator.class);

	private LocalSessionFactoryBean localSessionFactoryBean;

	private List types;

	private DescriptorFactory descriptorFactory;

	private HashMap<Class, IClassDescriptor> descriptors = new HashMap<Class, IClassDescriptor>();

	private int largeColumnLength = 100;

	public IClassDescriptor decorate(IClassDescriptor descriptor)
	{
		ArrayList<IPropertyDescriptor> decoratedPropertyDescriptors = new ArrayList<IPropertyDescriptor>();
		for (IPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors())
		{
			Class type = descriptor.getType();

			IPropertyDescriptor descriptorReference;
			try
			{
				ClassMetadata classMetaData = findMetadata(type);
				if (propertyDescriptor.getName().equals(getIdentifierProperty(type)))
				{
					descriptorReference = createIdentifierDescriptor(type, propertyDescriptor);
				} else if (notAHibernateProperty(classMetaData, propertyDescriptor))
				{
					// If this is not a hibernate property (i.e. marked
					// Transient), it's certainly not searchable
					// Are the any other properties like this?
					propertyDescriptor.setSearchable(false);
					descriptorReference = propertyDescriptor;
				} else
				{
					Property mappingProperty = getMapping(type).getProperty(propertyDescriptor.getName());
					descriptorReference = decoratePropertyDescriptor(type, mappingProperty, propertyDescriptor,
							descriptor);
				}

			} catch (HibernateException e)
			{
				throw new TrailsRuntimeException(e);
			}
			decoratedPropertyDescriptors.add(descriptorReference);
		}
		descriptor.setPropertyDescriptors(decoratedPropertyDescriptors);
		return descriptor;
	}

	protected IPropertyDescriptor decoratePropertyDescriptor(Class type, Property mappingProperty,
			IPropertyDescriptor descriptor, IClassDescriptor parentClassDescriptor)
	{
		if (isFormula(mappingProperty))
		{
			descriptor.setReadOnly(true);
			return descriptor;
		}
		descriptor.setLength(findColumnLength(mappingProperty));
		descriptor.setLarge(isLarge(mappingProperty));
		if (!mappingProperty.isOptional())
		{
			descriptor.setRequired(true);
		}

		if (!mappingProperty.isInsertable() && !mappingProperty.isUpdateable())
		{
			descriptor.setReadOnly(true);
		}

		IPropertyDescriptor descriptorReference = descriptor;
		Type hibernateType = mappingProperty.getType();
		if (mappingProperty.getType() instanceof ComponentType)
		{
			EmbeddedDescriptor embeddedDescriptor = buildEmbeddedDescriptor(type, mappingProperty, descriptor,
					parentClassDescriptor);
			descriptorReference = embeddedDescriptor;
		} else if (Collection.class.isAssignableFrom(descriptor.getPropertyType()))
		{
			descriptorReference = decorateCollectionDescriptor(type, descriptor, parentClassDescriptor);
		} else if (hibernateType.isAssociationType())
		{
			descriptorReference = decorateAssociationDescriptor(type, mappingProperty, descriptor,
					parentClassDescriptor);
		} else if (hibernateType.getReturnedClass().isEnum())
		{
			descriptor.addExtension(EnumReferenceDescriptor.class.getName(), new EnumReferenceDescriptor(hibernateType
					.getReturnedClass()));
		}

		return descriptorReference;
	}

	private EmbeddedDescriptor buildEmbeddedDescriptor(Class type, Property mappingProperty,
			IPropertyDescriptor descriptor, IClassDescriptor parentClassDescriptor)
	{
		Component componentMapping = (Component) mappingProperty.getValue();
		IClassDescriptor baseDescriptor = getDescriptorFactory().buildClassDescriptor(descriptor.getPropertyType());
		// build from base descriptor
		EmbeddedDescriptor embeddedDescriptor = new EmbeddedDescriptor(type, baseDescriptor);
		// and copy from property descriptor
		embeddedDescriptor.copyFrom(descriptor);
		ArrayList<IPropertyDescriptor> decoratedProperties = new ArrayList<IPropertyDescriptor>();
		// go thru each property and decorate it with Hibernate info
		for (IPropertyDescriptor propertyDescriptor : embeddedDescriptor.getPropertyDescriptors())
		{
			if (notAHibernateProperty(componentMapping, propertyDescriptor))
			{
				decoratedProperties.add(propertyDescriptor);
			} else
			{
				Property property = componentMapping.getProperty(propertyDescriptor.getName());
				IPropertyDescriptor iPropertyDescriptor = decoratePropertyDescriptor(embeddedDescriptor.getBeanType(),
						property, propertyDescriptor, parentClassDescriptor);
				decoratedProperties.add(iPropertyDescriptor);
			}
		}
		embeddedDescriptor.setPropertyDescriptors(decoratedProperties);
		return embeddedDescriptor;
	}

	/**
	 * The default way to order our property descriptors is by the order they
	 * appear in the hibernate config, with id first. Any non-mapped properties
	 * are tacked on at the end, til I think of a better way.
	 * 
	 * @param propertyDescriptors
	 * @return
	 */
	protected List sortPropertyDescriptors(Class type, List propertyDescriptors)
	{
		ArrayList sortedPropertyDescriptors = new ArrayList();

		try
		{
			sortedPropertyDescriptors.add(Ognl.getValue("#this.{? identifier == true}[0]", propertyDescriptors));
			for (Iterator iter = getMapping(type).getPropertyIterator(); iter.hasNext();)
			{
				Property mapping = (Property) iter.next();
				sortedPropertyDescriptors.addAll((List) Ognl.getValue("#this.{ ? name == \"" + mapping.getName()
						+ "\"}", propertyDescriptors));
			}
		} catch (Exception ex)
		{
			throw new TrailsRuntimeException(ex);
		}
		return sortedPropertyDescriptors;
	}

	/**
	 * Find the Hibernate metadata for this type, traversing up the hierarchy to
	 * supertypes if necessary
	 * 
	 * @param type
	 * @return
	 */
	protected ClassMetadata findMetadata(Class type) throws MetadataNotFoundException
	{
		ClassMetadata metaData = getSessionFactory().getClassMetadata(type);
		if (metaData != null)
		{
			return metaData;
		}
		if (!type.equals(Object.class))
		{
			return findMetadata(type.getSuperclass());
		} else
		{
			throw new MetadataNotFoundException("Failed to find metadata.");
		}
	}

	private boolean isFormula(Property mappingProperty)
	{
		for (Iterator iter = mappingProperty.getColumnIterator(); iter.hasNext();)
		{
			Selectable selectable = (Selectable) iter.next();
			if (selectable.isFormula())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if a property descriptor is in a component mapping
	 * 
	 * @param componentMapping
	 * @param propertyDescriptor
	 * @return true if the propertyDescriptor property is in componentMapping
	 */
	protected boolean notAHibernateProperty(Component componentMapping, IPropertyDescriptor propertyDescriptor)
	{
		for (Iterator iter = componentMapping.getPropertyIterator(); iter.hasNext();)
		{
			Property property = (Property) iter.next();
			if (property.getName().equals(propertyDescriptor.getName()))
			{
				return false;
			}
		}
		return true;
	}

	private boolean isLarge(Property mappingProperty)
	{
		// Hack to avoid setting large property if length
		// is exactly equal to Hibernate default column length
		return findColumnLength(mappingProperty) != Column.DEFAULT_LENGTH
				&& findColumnLength(mappingProperty) > getLargeColumnLength();
	}

	private int findColumnLength(Property mappingProperty)
	{
		int length = 0;
		for (Iterator iter = mappingProperty.getColumnIterator(); iter.hasNext();)
		{
			Column column = (Column) iter.next();
			length += column.getLength();
		}
		return length;
	}

	/**
	 * @param classMetaData
	 * @param type
	 * @return
	 */
	protected boolean notAHibernateProperty(ClassMetadata classMetaData, IPropertyDescriptor descriptor)
	{
		try
		{
			return (Boolean) Ognl.getValue("propertyNames.{ ? #this == \"" + descriptor.getName() + "\"}.size() == 0",
					classMetaData);
		} catch (OgnlException oe)
		{
			throw new TrailsRuntimeException(oe);
		}
	}

	/**
	 * @param type
	 * @param descriptor
	 * @param parentClassDescriptor
	 * @return
	 */
	private IdentifierDescriptor createIdentifierDescriptor(Class type, IPropertyDescriptor descriptor)
	{
		IdentifierDescriptor identifierDescriptor = new IdentifierDescriptor(type, descriptor);
		PersistentClass mapping = getMapping(type);

		if (((SimpleValue) mapping.getIdentifier()).getIdentifierGeneratorStrategy().equals("assigned"))
		{
			identifierDescriptor.setGenerated(false);
		}

		return identifierDescriptor;
	}

	/**
	 * @param type
	 * @return
	 */
	protected PersistentClass getMapping(Class type)
	{
		Configuration cfg = getLocalSessionFactoryBean().getConfiguration();

		return cfg.getClassMapping(type.getName());
	}

	/**
	 * @param type
	 * @param newDescriptor
	 */
	private CollectionDescriptor decorateCollectionDescriptor(Class type, IPropertyDescriptor descriptor,
			IClassDescriptor parentClassDescriptor)
	{
		try
		{
			CollectionDescriptor collectionDescriptor = new CollectionDescriptor(type, descriptor);
			org.hibernate.mapping.Collection collectionMapping = findCollectionMapping(type, descriptor.getName());
			// It is a child relationship if it has delete-orphan specified in
			// the mapping
			collectionDescriptor.setChildRelationship(collectionMapping.hasOrphanDelete());
			CollectionMetadata collectionMetaData = getSessionFactory().getCollectionMetadata(
					collectionMapping.getRole());

			collectionDescriptor.setElementType(collectionMetaData.getElementType().getReturnedClass());

			collectionDescriptor.setOneToMany(collectionMapping.isOneToMany());

			decorateOneToManyCollection(parentClassDescriptor, collectionDescriptor, collectionMapping);

			return collectionDescriptor;

		} catch (HibernateException e)
		{
			throw new TrailsRuntimeException(e);
		}
	}

	public IPropertyDescriptor decorateAssociationDescriptor(Class type, Property mappingProperty,
			IPropertyDescriptor descriptor, IClassDescriptor parentClassDescriptor)
	{
		Type hibernateType = mappingProperty.getType();
		Class parentClassType = parentClassDescriptor.getType();
		ObjectReferenceDescriptor descriptorReference = new ObjectReferenceDescriptor(type, descriptor, hibernateType
				.getReturnedClass());

		try
		{
			Field propertyField = parentClassType.getDeclaredField(descriptor.getName());
			PropertyDescriptor beanPropDescriptor = (PropertyDescriptor) Ognl.getValue(
					"propertyDescriptors.{? name == '" + descriptor.getName() + "'}[0]", Introspector
							.getBeanInfo(parentClassType));
			Method readMethod = beanPropDescriptor.getReadMethod();

			// Start by checking for and retrieving mappedBy attribute inside
			// the annotation
			String inverseProperty = "";
			if (readMethod.isAnnotationPresent(javax.persistence.OneToOne.class))
			{
				inverseProperty = readMethod.getAnnotation(javax.persistence.OneToOne.class).mappedBy();
			} else if (propertyField.isAnnotationPresent(javax.persistence.OneToOne.class))
			{
				inverseProperty = propertyField.getAnnotation(javax.persistence.OneToOne.class).mappedBy();
			} else
			{
				// If there is none then just return the
				// ObjectReferenceDescriptor
				return descriptorReference;
			}

			if ("".equals(inverseProperty))
			{
				// http://forums.hibernate.org/viewtopic.php?t=974287&sid=12d018b08dffe07e263652190cfc4e60
				// Caution... this does not support multiple
				// class references across the OneToOne relationship
				Class returnType = readMethod.getReturnType();
				for (int i = 0; i < returnType.getDeclaredMethods().length; i++)
				{
					if (returnType.getDeclaredMethods()[i].getReturnType().equals(propertyField.getDeclaringClass()))
					{
						Method theProperty = returnType.getDeclaredMethods()[i];
						/* strips preceding 'get' */
						inverseProperty = theProperty.getName().substring(3).toLowerCase();
						break;
					}
				}
			}

		} catch (SecurityException e)
		{
			LOG.error(e.getMessage());
		} catch (NoSuchFieldException e)
		{
			LOG.error(e.getMessage());
		} catch (OgnlException e)
		{
			LOG.error(e.getMessage());
		} catch (IntrospectionException e)
		{
			LOG.error(e.getMessage());
		}
		return descriptorReference;
	}

	/**
	 * I couldn't find a way to get the "mappedBy" value from the collection
	 * metadata, so I'm getting it from the OneToMany annotation.
	 */
	private void decorateOneToManyCollection(IClassDescriptor parentClassDescriptor,
			CollectionDescriptor collectionDescriptor, org.hibernate.mapping.Collection collectionMapping)
	{
		Class type = parentClassDescriptor.getType();
		if (collectionDescriptor.isOneToMany() && collectionMapping.isInverse())
		{
			try
			{

				Field propertyField = type.getDeclaredField(collectionDescriptor.getName());
				PropertyDescriptor beanPropDescriptor = (PropertyDescriptor) Ognl.getValue(
						"propertyDescriptors.{? name == '" + collectionDescriptor.getName() + "'}[0]", Introspector
								.getBeanInfo(type));
				Method readMethod = beanPropDescriptor.getReadMethod();
				String mappedBy = "";
				if (readMethod.isAnnotationPresent(javax.persistence.OneToMany.class))
				{
					mappedBy = readMethod.getAnnotation(javax.persistence.OneToMany.class).mappedBy();
				} else if (propertyField.isAnnotationPresent(javax.persistence.OneToMany.class))
				{
					mappedBy = propertyField.getAnnotation(javax.persistence.OneToMany.class).mappedBy();
				}

				if (!"".equals(mappedBy))
				{
					collectionDescriptor.setInverseProperty(mappedBy);
				}

				parentClassDescriptor.setHasCyclicRelationships(true);

			} catch (SecurityException e)
			{
				LOG.error(e.getMessage());
			} catch (NoSuchFieldException e)
			{
				LOG.error(e.getMessage());
			} catch (OgnlException e)
			{
				LOG.error(e.getMessage());
			} catch (IntrospectionException e)
			{
				LOG.error(e.getMessage());
			}
		}
	}

	protected org.hibernate.mapping.Collection findCollectionMapping(Class type, String name)
	{
		String roleName = type.getName() + "." + name;
		org.hibernate.mapping.Collection collectionMapping = getLocalSessionFactoryBean().getConfiguration()
				.getCollectionMapping(roleName);
		if (collectionMapping != null)
		{
			return collectionMapping;
		} else if (!type.equals(Object.class))
		{
			return findCollectionMapping(type.getSuperclass(), name);
		} else
		{
			throw new MetadataNotFoundException("Metadata not found.");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.trails.descriptor.PropertyDescriptorService#getIdentifierProperty(java.lang.Class)
	 */
	public String getIdentifierProperty(Class type)
	{
		try
		{
			return getSessionFactory().getClassMetadata(type).getIdentifierPropertyName();
		} catch (HibernateException e)
		{
			throw new TrailsRuntimeException(e);
		}
	}

	/**
	 * @return Returns the sessionFactory.
	 */
	public SessionFactory getSessionFactory()
	{
		return (SessionFactory) getLocalSessionFactoryBean().getObject();
	}

	public IClassDescriptor getClassDescriptor(Class type)
	{
		return descriptors.get(type);
	}

	/**
	 * @return Returns the localSessionFactoryBean.
	 */
	public LocalSessionFactoryBean getLocalSessionFactoryBean()
	{
		return localSessionFactoryBean;
	}

	/**
	 * @param localSessionFactoryBean
	 *            The localSessionFactoryBean to set.
	 */
	public void setLocalSessionFactoryBean(LocalSessionFactoryBean localSessionFactoryBean)
	{
		this.localSessionFactoryBean = localSessionFactoryBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.trails.descriptor.TrailsDescriptorService#getAllDescriptors()
	 */
	public List<IClassDescriptor> getAllDescriptors()
	{
		return new ArrayList<IClassDescriptor>(descriptors.values());
	}

	public List getTypes()
	{
		return types;
	}

	public void setTypes(List types)
	{
		this.types = types;
	}

	public int getLargeColumnLength()
	{
		return largeColumnLength;
	}

	/**
	 * Columns longer than this will have their large property set to true.
	 * 
	 * @param largeColumnLength
	 */
	public void setLargeColumnLength(int largeColumnLength)
	{
		this.largeColumnLength = largeColumnLength;
	}

	public DescriptorFactory getDescriptorFactory()
	{
		return descriptorFactory;
	}

	public void setDescriptorFactory(DescriptorFactory descriptorFactory)
	{
		this.descriptorFactory = descriptorFactory;
	}

}
