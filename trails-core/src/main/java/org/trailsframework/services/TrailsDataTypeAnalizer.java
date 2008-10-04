package org.trailsframework.services;

import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.descriptor.IPropertyDescriptor;

/**
 * The default data type analyzer, which is based entirely on the type of the property (and not on annotations or naming
 * conventions). This is based on a configuration of property type class to string provided as an IoC service
 * configuration.
 */
public class TrailsDataTypeAnalizer implements DataTypeAnalyzer
{

	private DescriptorService descriptorService;

	public TrailsDataTypeAnalizer(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}

	public String identifyDataType(PropertyAdapter adapter)
	{

		IPropertyDescriptor propertyDescriptor = getPropertyDescriptor(adapter);

		if (propertyDescriptor != null && propertyDescriptor.isObjectReference())
		{
			return ("referenceEditor");
		}

		// To avoid "no strategy" exceptions, we expect a contribution of Object.class to the empty
		// string. We convert that back to a null.
		return null;
	}

	private IPropertyDescriptor getPropertyDescriptor(PropertyAdapter adapter)
	{

		IClassDescriptor classDescriptor = descriptorService.getClassDescriptor(adapter.getBeanType());
		IPropertyDescriptor propertyDescriptor = null;

		if (classDescriptor != null)
		{
			propertyDescriptor = classDescriptor.getPropertyDescriptor(adapter.getName());
		}

		return propertyDescriptor;
	}

}

