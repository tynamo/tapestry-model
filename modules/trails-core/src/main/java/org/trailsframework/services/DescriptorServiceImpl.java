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
 * This class builds and caches IClassDescriptors.
 * Descriptors are build during application startup.
 */
public class DescriptorServiceImpl implements DescriptorService
{
	private final StrategyRegistry<IClassDescriptor> descriptorsRegistry;
	private final List<IClassDescriptor> descriptors;

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
		Map<Class, IClassDescriptor> descriptorsMap = new HashMap<Class, IClassDescriptor>();
		for (Class type : types)
		{
			descriptorsMap.put(type, descriptorFactory.buildClassDescriptor(type));
		}

		this.descriptors = CollectionFactory.newList(descriptorsMap.values());
		this.descriptorsRegistry = StrategyRegistry.newInstance(IClassDescriptor.class, descriptorsMap);

		// second pass to find children
		for (IClassDescriptor iClassDescriptor : descriptorsMap.values())
		{
			findChildren(iClassDescriptor);
		}

	}

	public List<IClassDescriptor> getAllDescriptors()
	{
		return descriptors;
	}

	public IClassDescriptor getClassDescriptor(Class type)
	{
		Defense.notNull(type, "type");
		return descriptorsRegistry.get(type);
	}

	private void findChildren(IClassDescriptor iClassDescriptor)
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

}
