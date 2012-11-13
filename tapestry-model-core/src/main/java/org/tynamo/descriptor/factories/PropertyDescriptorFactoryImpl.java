package org.tynamo.descriptor.factories;

import org.apache.commons.beanutils.BeanUtils;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptorImpl;
import org.tynamo.util.Utils;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;


public class PropertyDescriptorFactoryImpl implements PropertyDescriptorFactory
{

	private final Collection<String> propertyExcludes;

	public PropertyDescriptorFactoryImpl(Collection<String> propertyExcludes)
	{
		this.propertyExcludes = propertyExcludes;
	}


	/**
	 * Build the set of property descriptors for this type
	 *
	 * @param beanType the aggregating class
	 * @param beanInfo the BeanInfo, already gathered
	 * @return
	 * @throws Exception
	 */
	public ArrayList<TynamoPropertyDescriptor> buildPropertyDescriptors(Class beanType, BeanInfo beanInfo) throws InvocationTargetException, IllegalAccessException
	{
		ArrayList<TynamoPropertyDescriptor> result = new ArrayList<TynamoPropertyDescriptor>();
		for (PropertyDescriptor beanPropDescriptor : beanInfo.getPropertyDescriptors())
		{
			if (!Utils.isExcluded(beanPropDescriptor.getName(), propertyExcludes))
			{
				Class<?> propertyType = beanPropDescriptor.getPropertyType();
				TynamoPropertyDescriptorImpl propDescriptor = new TynamoPropertyDescriptorImpl(beanType, propertyType);
				BeanUtils.copyProperties(propDescriptor, beanPropDescriptor);
				result.add(propDescriptor);
			}
		}
		return result;
	}
}
