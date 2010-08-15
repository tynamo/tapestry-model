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
package org.tynamo.hibernate.services;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.tynamo.descriptor.*;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.descriptor.extension.EnumReferenceDescriptor;
import org.tynamo.descriptor.factories.DescriptorFactory;
import org.tynamo.exception.MetadataNotFoundException;
import org.tynamo.exception.TynamoRuntimeException;
import org.tynamo.hibernate.TynamoHibernateSymbols;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This decorator will add metadata information. It will replace simple
 * reflection based TynamoPropertyTynamoPropertyDescriptors with appropriate
 * Hibernate descriptors <p/> Background... TynamoDescriptorService operates one
 * ReflectorDescriptorFactory - TynamoDescriptorService iterates/scans all class
 * types encountered - ReflectorDescriptorFactory allocates property descriptor
 * instance for the class type - TynamoDescriptorService decorates property
 * descriptor by calling this module HibernateDescriptorDecorator -
 * HibernateDescriptorDecorator caches the decorated property descriptor into a
 * decorated descriptor list - decorated descriptor list gets populated into
 * class descriptor for class type - TynamoDescriptorService finally populates
 * decorated class descriptor and it's aggregated list of decorated property
 * descriptors into it's own list/cache of referenced class descriptors
 *
 * @see TynamoPropertyDescriptor
 * @see ObjectReferenceDescriptor
 * @see CollectionDescriptor
 * @see EmbeddedDescriptor
 */
public class HibernateDescriptorDecorator implements DescriptorDecorator
{
	private Logger logger;

	private HibernateSessionSource hibernateSessionSource;

	private DescriptorFactory descriptorFactory;

	/**
	 * Columns longer than this will have their large property set to true.
	 */
	private final int largeColumnLength;

	private final boolean ignoreNonHibernateTypes;

	public HibernateDescriptorDecorator(HibernateSessionSource hibernateSessionSource,
	                                    DescriptorFactory descriptorFactory,
	                                    @Inject @Symbol(TynamoHibernateSymbols.LARGE_COLUMN_LENGTH)
	                                    int largeColumnLength,
	                                    @Inject @Symbol(TynamoHibernateSymbols.IGNORE_NON_HIBERNATE_TYPES)
	                                    boolean ignoreNonHibernateTypes,
	                                    Logger logger)
	{
		this.hibernateSessionSource = hibernateSessionSource;
		this.descriptorFactory = descriptorFactory;
		this.largeColumnLength = largeColumnLength;
		this.ignoreNonHibernateTypes = ignoreNonHibernateTypes;
		this.logger = logger;
	}

	public TynamoClassDescriptor decorate(TynamoClassDescriptor descriptor)
	{
		ArrayList<TynamoPropertyDescriptor> decoratedPropertyDescriptors = new ArrayList<TynamoPropertyDescriptor>();

		Class type = descriptor.getType();
		ClassMetadata classMetaData = null;

		try
		{
			classMetaData = findMetadata(type);
		} catch (MetadataNotFoundException e)
		{
			if (ignoreNonHibernateTypes)
			{
				logger.warn("MetadataNotFound! Ignoring:" + descriptor.getType().toString());
				descriptor.setHidden(true);
				return descriptor;
			} else
			{
				throw new TynamoRuntimeException(e);
			}
		}

		for (TynamoPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors())
		{
			try
			{
				TynamoPropertyDescriptor descriptorReference;

				if (propertyDescriptor.getName().equals(getIdentifierProperty(type)))
				{
					descriptorReference = createIdentifierDescriptor(type, propertyDescriptor, descriptor);
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

				decoratedPropertyDescriptors.add(descriptorReference);

			} catch (HibernateException e)
			{
				throw new TynamoRuntimeException(e);
			}
		}
		descriptor.setPropertyDescriptors(decoratedPropertyDescriptors);
		return descriptor;
	}

	protected TynamoPropertyDescriptor decoratePropertyDescriptor(Class type, Property mappingProperty,
	                                                              TynamoPropertyDescriptor descriptor,
	                                                              TynamoClassDescriptor parentClassDescriptor)
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

		TynamoPropertyDescriptor descriptorReference = descriptor;
		Type hibernateType = mappingProperty.getType();
		if (mappingProperty.getType() instanceof ComponentType)
		{
			descriptorReference = buildEmbeddedDescriptor(type, mappingProperty, descriptor, parentClassDescriptor);
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
	                                                   TynamoPropertyDescriptor descriptor,
	                                                   TynamoClassDescriptor parentClassDescriptor)
	{
		Component componentMapping = (Component) mappingProperty.getValue();
		TynamoClassDescriptor baseDescriptor = descriptorFactory.buildClassDescriptor(descriptor.getPropertyType());
		// build from base descriptor
		EmbeddedDescriptor embeddedDescriptor = new EmbeddedDescriptor(type, baseDescriptor);
		// and copy from property descriptor
		embeddedDescriptor.copyFrom(descriptor);
		ArrayList<TynamoPropertyDescriptor> decoratedProperties = new ArrayList<TynamoPropertyDescriptor>();
		// go thru each property and decorate it with Hibernate info
		for (TynamoPropertyDescriptor propertyDescriptor : embeddedDescriptor.getPropertyDescriptors())
		{
			if (notAHibernateProperty(componentMapping, propertyDescriptor))
			{
				decoratedProperties.add(propertyDescriptor);
			} else
			{
				Property property = componentMapping.getProperty(propertyDescriptor.getName());
				TynamoPropertyDescriptor tynamopropertydescriptor =
						decoratePropertyDescriptor(embeddedDescriptor.getBeanType(),
								property, propertyDescriptor, parentClassDescriptor);
				decoratedProperties.add(tynamopropertydescriptor);
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
			throw new TynamoRuntimeException(ex);
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
		ClassMetadata metaData = hibernateSessionSource.getSessionFactory().getClassMetadata(type);
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
	protected boolean notAHibernateProperty(Component componentMapping, TynamoPropertyDescriptor propertyDescriptor)
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
				&& findColumnLength(mappingProperty) > largeColumnLength;
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
	 * @param descriptor
	 * @return
	 */
	protected boolean notAHibernateProperty(ClassMetadata classMetaData, TynamoPropertyDescriptor descriptor)
	{
		try
		{
			return (Boolean) Ognl.getValue("propertyNames.{ ? #this == \"" + descriptor.getName() + "\"}.size() == 0",
					classMetaData);
		} catch (OgnlException oe)
		{
			throw new TynamoRuntimeException(oe);
		}
	}

	/**
	 * @param type
	 * @param descriptor
	 * @param parentClassDescriptor
	 * @return
	 */
	private IdentifierDescriptor createIdentifierDescriptor(Class type, TynamoPropertyDescriptor descriptor,
	                                                        TynamoClassDescriptor parentClassDescriptor)
	{
		IdentifierDescriptor identifierDescriptor;
		PersistentClass mapping = getMapping(type);

		/**
		 * fix for TRAILS-92
		 */
		if (mapping.getProperty(descriptor.getName()).getType() instanceof ComponentType)
		{
			EmbeddedDescriptor embeddedDescriptor = buildEmbeddedDescriptor(type,
					mapping.getProperty(descriptor.getName()), descriptor, parentClassDescriptor);
			embeddedDescriptor.setIdentifier(true);
			identifierDescriptor = embeddedDescriptor;
		} else
		{
			identifierDescriptor = new IdentifierDescriptorImpl(type, descriptor);
		}

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
		Configuration cfg = hibernateSessionSource.getConfiguration();

		return cfg.getClassMapping(type.getName());
	}

	/**
	 * @param type
	 * @param descriptor
	 * @param parentClassDescriptor
	 */
	private CollectionDescriptor decorateCollectionDescriptor(Class type, TynamoPropertyDescriptor descriptor,
	                                                          TynamoClassDescriptor parentClassDescriptor)
	{
		try
		{
			CollectionDescriptor collectionDescriptor = new CollectionDescriptor(type, descriptor);
			org.hibernate.mapping.Collection collectionMapping = findCollectionMapping(type, descriptor.getName());
			// It is a child relationship if it has delete-orphan specified in
			// the mapping
			collectionDescriptor.setChildRelationship(collectionMapping.hasOrphanDelete());
			CollectionMetadata collectionMetaData = hibernateSessionSource.getSessionFactory().getCollectionMetadata(
					collectionMapping.getRole());

			collectionDescriptor.setElementType(collectionMetaData.getElementType().getReturnedClass());

			collectionDescriptor.setOneToMany(collectionMapping.isOneToMany());

			decorateOneToManyCollection(parentClassDescriptor, collectionDescriptor, collectionMapping);

			return collectionDescriptor;

		} catch (HibernateException e)
		{
			throw new TynamoRuntimeException(e);
		}
	}

	public TynamoPropertyDescriptor decorateAssociationDescriptor(Class type, Property mappingProperty,
	                                                              TynamoPropertyDescriptor descriptor,
	                                                              TynamoClassDescriptor parentClassDescriptor)
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
			logger.error(e.getMessage());
		} catch (NoSuchFieldException e)
		{
			logger.error(e.getMessage());
		} catch (OgnlException e)
		{
			logger.error(e.getMessage());
		} catch (IntrospectionException e)
		{
			logger.error(e.getMessage());
		}
		return descriptorReference;
	}

	/**
	 * I couldn't find a way to get the "mappedBy" value from the collection
	 * metadata, so I'm getting it from the OneToMany annotation.
	 */
	private void decorateOneToManyCollection(TynamoClassDescriptor parentClassDescriptor,
	                                         CollectionDescriptor collectionDescriptor,
	                                         org.hibernate.mapping.Collection collectionMapping)
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
				logger.error(e.getMessage());
				e.printStackTrace();
			} catch (NoSuchFieldException e)
			{
				logger.error(e.getMessage());
				e.printStackTrace();
			} catch (OgnlException e)
			{
				logger.error(e.getMessage());
				e.printStackTrace();
			} catch (IntrospectionException e)
			{
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	protected org.hibernate.mapping.Collection findCollectionMapping(Class type, String name)
	{
		String roleName = type.getName() + "." + name;
		org.hibernate.mapping.Collection collectionMapping = hibernateSessionSource.getConfiguration()
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
	 * @see org.tynamo.descriptor.PropertyDescriptorService#getIdentifierProperty(java.lang.Class)
	 */

	public String getIdentifierProperty(Class type)
	{
		try
		{
			return hibernateSessionSource.getSessionFactory().getClassMetadata(type).getIdentifierPropertyName();
		} catch (HibernateException e)
		{
			throw new TynamoRuntimeException(e);
		}
	}
}
