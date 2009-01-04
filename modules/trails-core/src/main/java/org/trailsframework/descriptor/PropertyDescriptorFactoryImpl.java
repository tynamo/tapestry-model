package org.trailsframework.descriptor;

import org.apache.commons.beanutils.BeanUtils;
import org.trailsframework.util.Utils;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
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
	public ArrayList<IPropertyDescriptor> buildPropertyDescriptors(Class beanType, BeanInfo beanInfo) throws Exception
	{
		ArrayList<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		for (PropertyDescriptor beanPropDescriptor : beanInfo.getPropertyDescriptors())
		{
			if (!Utils.isExcluded(beanPropDescriptor.getName(), propertyExcludes))
			{
				Class<?> propertyType = beanPropDescriptor.getPropertyType();
				TrailsPropertyDescriptor propDescriptor = new TrailsPropertyDescriptor(beanType, propertyType);
				BeanUtils.copyProperties(propDescriptor, beanPropDescriptor);
				result.add(propDescriptor);
			}
		}
		return result;
	}
}
