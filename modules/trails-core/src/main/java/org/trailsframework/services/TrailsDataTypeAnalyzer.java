package org.trailsframework.services;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.descriptor.IPropertyDescriptor;

import java.util.Map;

/**
 * The default data type analyzer, which is based entirely on the type of the property (and not on annotations or naming
 * conventions). This is based on a configuration of property type class to string provided as an IoC service
 * configuration.
 */
public class TrailsDataTypeAnalyzer implements DataTypeAnalyzer
{

	private final DescriptorService descriptorService;
	private final Map<String, String> editorMap;

	public TrailsDataTypeAnalyzer(final DescriptorService descriptorService, final Map<String, String> editorMap)
	{
		this.descriptorService = descriptorService;
		this.editorMap = editorMap;
	}

	public String identifyDataType(PropertyAdapter adapter)
	{
		IPropertyDescriptor propertyDescriptor = getPropertyDescriptor(adapter);
		for (Map.Entry<String, String> entry : editorMap.entrySet())
		{
			try
			{
				if ((Boolean) Ognl.getValue(entry.getKey(), propertyDescriptor))
				{
					return entry.getValue();
				}
			} catch (OgnlException e)
			{
				//do nothing;
			}
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

