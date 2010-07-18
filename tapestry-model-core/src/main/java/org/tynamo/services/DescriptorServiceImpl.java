package org.tynamo.services;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.apache.tapestry5.ioc.util.StrategyRegistry;
import org.tynamo.descriptor.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class builds and caches TynamoClassDescriptors.
 * Descriptors are build during application startup.
 */
public class DescriptorServiceImpl implements DescriptorService
{
	private final StrategyRegistry<TynamoClassDescriptor> descriptorsRegistry;
	private final List<TynamoClassDescriptor> descriptors;

	/**
	 * For each class in types, a descriptor is built by the DescriptorFactory.  Next it is decorated
	 * by each DescriptorDecorator in turn.  Finally it is cached.
	 *
	 * @param types			 In the Tynamo default configuration this will be set to all classes in the Hibernate config
	 * @param descriptorFactory In default Tynamo this will be a ReflectionDescriptorFactory
	 * @see DescriptorFactory
	 * @see DescriptorDecorator
	 */
	public DescriptorServiceImpl(final Collection<Class> types, DescriptorFactory descriptorFactory)
	{
		Map<Class, TynamoClassDescriptor> descriptorsMap = new HashMap<Class, TynamoClassDescriptor>();
		for (Class type : types)
		{
			descriptorsMap.put(type, descriptorFactory.buildClassDescriptor(type));
		}

		this.descriptors = CollectionFactory.newList(descriptorsMap.values());
		this.descriptorsRegistry = StrategyRegistry.newInstance(TynamoClassDescriptor.class, descriptorsMap, true);

		// second pass to find children
		for (TynamoClassDescriptor classDescriptor : descriptorsMap.values())
		{
			findChildren(classDescriptor);
		}

	}

	public List<TynamoClassDescriptor> getAllDescriptors()
	{
		return descriptors;
	}

	public TynamoClassDescriptor getClassDescriptor(Class type)
	{
		return descriptorsRegistry.get(type);
	}

	private void findChildren(TynamoClassDescriptor classDescriptor)
	{
		for (TynamoPropertyDescriptor propertyDescriptor : classDescriptor.getPropertyDescriptors())
		{
			if (propertyDescriptor.isCollection())
			{
				if (((CollectionDescriptor) propertyDescriptor).isChildRelationship())
				{
					TynamoClassDescriptor collectionClassDescriptor = getClassDescriptor(((CollectionDescriptor) propertyDescriptor).getElementType());
					collectionClassDescriptor.setChild(true);
				}
				if (((CollectionDescriptor) propertyDescriptor).getInverseProperty() != null)
				{
					classDescriptor.setHasCyclicRelationships(true);
				}
			}
		}
	}

}
