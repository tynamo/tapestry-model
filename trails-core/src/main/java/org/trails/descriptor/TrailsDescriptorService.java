package org.trails.descriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.OgnlException;

/**
 * This class builds and caches IClassDescriptors.  Descriptors are build during the init
 * method which is called by Spring during application startup
 *
 * @author cnelson
 * @see IClassDescriptor
 */
public class TrailsDescriptorService implements DescriptorService
{
	protected List<Class> types;
	protected Map<Class, IClassDescriptor> descriptors = new HashMap<Class, IClassDescriptor>();
	private List<DescriptorDecorator> decorators = new ArrayList<DescriptorDecorator>();
	private DescriptorFactory descriptorFactory;

	/**
	 * For each class in types, a descriptor is built by the DescriptorFactory.  Next it is decorated
	 * by each DescriptorDecorator in turn.  Finally it is cached.
	 *
	 * @throws OgnlException
	 * @see DescriptorFactory
	 * @see DescriptorDecorator
	 */
	public void init() throws OgnlException
	{
		descriptors.clear();
		for (Class type : types)
		{
			IClassDescriptor descriptor = getDescriptorFactory().buildClassDescriptor(type);
			descriptor = applyDecorators(descriptor);
			descriptors.put(type, descriptor);
		}
		// second pass to find children and set up descriptor parents
		for (IClassDescriptor iClassDescriptor : descriptors.values())
		{
			findChildren(iClassDescriptor);
			// set up the property parents, could be avoided if there was a proper symbol table
			for (IPropertyDescriptor iPropertyDescriptor : iClassDescriptor.getPropertyDescriptors())
			{
				iPropertyDescriptor.setParentClassDescriptor(descriptors.get(iPropertyDescriptor.getPropertyType()));
			}
		}
	}

	public List getAllDescriptors()
	{
		List<IClassDescriptor> allDescriptors = new ArrayList<IClassDescriptor>(descriptors.values());
		Collections.sort(allDescriptors, new Comparator<IClassDescriptor>()
		{
			public int compare(IClassDescriptor o1, IClassDescriptor o2)
			{
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
		return allDescriptors;
	}

	/* (non-Javadoc)
		 * @see org.trails.descriptor.IDescriptorFactory#buildClassDescriptor(java.lang.Class)
		 */
	public IClassDescriptor getClassDescriptor(Class type)
	{
		if (type.getName().contains("CGLIB"))
		{
			return descriptors.get(type.getSuperclass());
		} else
		{
			return descriptors.get(type);
		}
	}

	protected void findChildren(IClassDescriptor iClassDescriptor)
	{
		for (IPropertyDescriptor propertyDescriptor : iClassDescriptor.getPropertyDescriptors())
		{
			if (propertyDescriptor.isCollection())
			{
				if (((CollectionDescriptor) propertyDescriptor).isChildRelationship())
				{
					IClassDescriptor collectionClassDescriptor = getClassDescriptor(((CollectionDescriptor) propertyDescriptor).getElementType());
					collectionClassDescriptor.setChild(true);
				}
				if (((CollectionDescriptor) propertyDescriptor).getInverseProperty() != null)
				{
					iClassDescriptor.setHasCyclicRelationships(true);
				}
			}
		}
	}

	/**
	 * Have the decorators decorate this descriptor  todo what are decorators for and/or why would I want to call this
	 * menthod?
	 *
	 * @param descriptor
	 * @return The resulting descriptor after all decorators are applied
	 */
	protected IClassDescriptor applyDecorators(IClassDescriptor descriptor)
	{
		IClassDescriptor currDescriptor = descriptor;
		for (DescriptorDecorator decorator : getDecorators())
		{
			currDescriptor = decorator.decorate(currDescriptor);
		}
		return currDescriptor;
	}

	/**
	 * In the Trails default configuration this will be set
	 * to all classes in the Hibernate config
	 *
	 * @return
	 */
	public List getTypes()
	{
		return types;
	}

	/**
	 * @param types all the classes this service should describe
	 */
	public void setTypes(List<Class> types)
	{
		this.types = types;
	}

	/**
	 * In the default Trails configuration this will contain a HibernateDescriptorDecorator
	 * and an AnnotationDecorator
	 *
	 * @return
	 * @see org.trails.hibernate.HibernateDescriptorDecorator
	 * @see org.trails.descriptor.annotation.AnnotationDecorator
	 */
	public List<DescriptorDecorator> getDecorators()
	{
		return decorators;
	}

	public void setDecorators(List<DescriptorDecorator> decorators)
	{
		this.decorators = decorators;
	}

	/**
	 * In default Trails this will be a ReflectionDescriptorFactory
	 *
	 * @return
	 * @see ReflectionDescriptorFactory
	 */
	public DescriptorFactory getDescriptorFactory()
	{
		return descriptorFactory;
	}

	public void setDescriptorFactory(DescriptorFactory descriptorFactory)
	{
		this.descriptorFactory = descriptorFactory;
	}
}
