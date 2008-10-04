package org.trailsframework.services;

import org.trailsframework.descriptor.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.EntityMode;
import org.apache.tapestry5.hibernate.HibernateSessionSource;

import java.util.*;

/**
 * This class builds and caches IClassDescriptors.
 * Descriptors are build during application startup.
 *
 * @author cnelson
 * @see org.trailsframework.descriptor.IClassDescriptor
 */
public class DescriptorServiceImpl implements DescriptorService
{
	protected Map<Class, IClassDescriptor> descriptors;

	/**
	 *
	 * @param types			* In the Trails default configuration this will be set to all classes in the Hibernate config
	 * @param decorators	  * In the default Trails configuration this will contain a HibernateDescriptorDecorator  and an AnnotationDecorator
	 * @param descriptorFactory	  * In default Trails this will be a ReflectionDescriptorFactory
	 */
	public DescriptorServiceImpl(HibernateSessionSource hibernateSessionSource, List<DescriptorDecorator> decorators, DescriptorFactory descriptorFactory)
	{

		ArrayList<Class> types = new ArrayList<Class>();
		for (Object classMetadata : hibernateSessionSource.getSessionFactory().getAllClassMetadata().values())
		{
			types.add(((ClassMetadata) classMetadata).getMappedClass(EntityMode.POJO));
		}

		/**
		 * For each class in types, a descriptor is built by the DescriptorFactory.  Next it is decorated
		 * by each DescriptorDecorator in turn.  Finally it is cached.
		 *
		 * @see DescriptorFactory
		 * @see DescriptorDecorator
		 */
		descriptors = new HashMap<Class, IClassDescriptor>();
		for (Class type : types)
		{
			IClassDescriptor descriptor = descriptorFactory.buildClassDescriptor(type);
			descriptor = applyDecorators(descriptor, decorators);
			descriptors.put(type, descriptor);
		}
		// second pass to find children and set up descriptor parents
		for (IClassDescriptor iClassDescriptor : descriptors.values())
		{
			findChildren(iClassDescriptor);
		}
	}

	public List<IClassDescriptor> getAllDescriptors()
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
		 * @see org.trailsframework.descriptor.IDescriptorFactory#buildClassDescriptor(java.lang.Class)
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
	 * Have the decorators decorate this descriptor
	 *
	 * @param descriptor
	 * @param decorators
	 * @return The resulting descriptor after all decorators are applied
	 */
	protected IClassDescriptor applyDecorators(IClassDescriptor descriptor, List<DescriptorDecorator> decorators)
	{
		IClassDescriptor currDescriptor = descriptor;
		for (DescriptorDecorator decorator : decorators)
		{
			currDescriptor = decorator.decorate(currDescriptor);
		}
		return currDescriptor;
	}
}
