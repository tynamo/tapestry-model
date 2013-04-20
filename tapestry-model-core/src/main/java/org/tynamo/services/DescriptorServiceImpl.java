package org.tynamo.services;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.util.StrategyRegistry;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.EmbeddedDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.factories.DescriptorFactory;
import org.tynamo.exception.TynamoRuntimeException;

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
	 * @param types In the Tynamo default configuration this will be set to all classes in the Hibernate config
	 * @param descriptorFactory In default Tynamo this will be a ReflectionDescriptorFactory
	 * @see org.tynamo.descriptor.factories.DescriptorFactory
	 * @see org.tynamo.descriptor.decorators.DescriptorDecorator
	 */
	public DescriptorServiceImpl(final Collection<Class> types, DescriptorFactory descriptorFactory)
	{
		final Map<Class, TynamoClassDescriptor> descriptorsMap = new HashMap<Class, TynamoClassDescriptor>();

		for (Class type : types)
		{
			try
			{

				TynamoClassDescriptor classDescriptor = descriptorFactory.buildClassDescriptor(type);
				descriptorsMap.put(type, classDescriptor);

			} catch (TynamoRuntimeException e)
			{
				// smile, do nothing, move on.
			}
		}

		// second pass to find children
		for (Class type : types)
		{
			findChildren(type, descriptorsMap);
		}

		this.descriptors = CollectionFactory.newList(descriptorsMap.values());
		this.descriptorsRegistry = StrategyRegistry.newInstance(TynamoClassDescriptor.class, descriptorsMap, true);

	}

	public List<TynamoClassDescriptor> getAllDescriptors()
	{
		return descriptors;
	}

	public TynamoClassDescriptor getClassDescriptor(Class type)
	{
		return descriptorsRegistry.get(type);
	}

	private static void findChildren(Class type, Map<Class, TynamoClassDescriptor> descriptorsMap)
	{
		TynamoClassDescriptor classDescriptor = descriptorsMap.get(type);

		for (TynamoPropertyDescriptor propertyDescriptor : classDescriptor.getPropertyDescriptors())
		{
			if (propertyDescriptor.isCollection())
			{
				if (((CollectionDescriptor) propertyDescriptor).isChildRelationship())
				{
					TynamoClassDescriptor collectionClassDescriptor = descriptorsMap.get(((CollectionDescriptor) propertyDescriptor).getElementType());
					collectionClassDescriptor.setChild(true);
				}
				if (((CollectionDescriptor) propertyDescriptor).getInverseProperty() != null)
				{
					classDescriptor.setHasCyclicRelationships(true);
				}
			}

			if (propertyDescriptor.isEmbedded() && !descriptorsMap.containsKey(propertyDescriptor.getPropertyType()))
			{
				TynamoClassDescriptor tynamoClassDescriptor = ((EmbeddedDescriptor) propertyDescriptor).getEmbeddedClassDescriptor();
				descriptorsMap.put(tynamoClassDescriptor.getBeanType(), tynamoClassDescriptor);
			}
		}
	}
}
