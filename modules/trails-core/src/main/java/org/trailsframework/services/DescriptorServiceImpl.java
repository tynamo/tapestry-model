package org.trailsframework.services;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.ioc.util.StrategyRegistry;
import org.trailsframework.descriptor.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class builds and caches TrailsClassDescriptors.
 * Descriptors are build during application startup.
 */
public class DescriptorServiceImpl implements DescriptorService
{
	private final StrategyRegistry<TrailsClassDescriptor> descriptorsRegistry;
	private final List<TrailsClassDescriptor> descriptors;

	/**
	 * For each class in types, a descriptor is built by the DescriptorFactory.  Next it is decorated
	 * by each DescriptorDecorator in turn.  Finally it is cached.
	 *
	 * @param types			 In the Trails default configuration this will be set to all classes in the Hibernate config
	 * @param descriptorFactory In default Trails this will be a ReflectionDescriptorFactory
	 * @see DescriptorFactory
	 * @see DescriptorDecorator
	 */
	public DescriptorServiceImpl(final Collection<Class> types, DescriptorFactory descriptorFactory)
	{
		Map<Class, TrailsClassDescriptor> descriptorsMap = new HashMap<Class, TrailsClassDescriptor>();
		for (Class type : types)
		{
			descriptorsMap.put(type, descriptorFactory.buildClassDescriptor(type));
		}

		this.descriptors = CollectionFactory.newList(descriptorsMap.values());
		this.descriptorsRegistry = StrategyRegistry.newInstance(TrailsClassDescriptor.class, descriptorsMap, true);

		// second pass to find children
		for (TrailsClassDescriptor TrailsClassDescriptor : descriptorsMap.values())
		{
			findChildren(TrailsClassDescriptor);
		}

	}

	public List<TrailsClassDescriptor> getAllDescriptors()
	{
		return descriptors;
	}

	public TrailsClassDescriptor getClassDescriptor(Class type)
	{
		Defense.notNull(type, "type");
		return descriptorsRegistry.get(type);
	}

	private void findChildren(TrailsClassDescriptor TrailsClassDescriptor)
	{
		for (TrailsPropertyDescriptor propertyDescriptor : TrailsClassDescriptor.getPropertyDescriptors())
		{
			if (propertyDescriptor.isCollection())
			{
				if (((CollectionDescriptor) propertyDescriptor).isChildRelationship())
				{
					TrailsClassDescriptor collectionClassDescriptor = getClassDescriptor(((CollectionDescriptor) propertyDescriptor).getElementType());
					collectionClassDescriptor.setChild(true);
				}
				if (((CollectionDescriptor) propertyDescriptor).getInverseProperty() != null)
				{
					TrailsClassDescriptor.setHasCyclicRelationships(true);
				}
			}
		}
	}

}
