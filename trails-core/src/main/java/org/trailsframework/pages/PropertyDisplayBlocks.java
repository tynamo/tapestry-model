package org.trailsframework.pages;

import org.trailsframework.descriptor.TrailsPropertyDescriptor;
import org.apache.tapestry5.services.PropertyOutputContext;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;

public class PropertyDisplayBlocks
{

	@Environmental
	@Property(write = false)
	private PropertyOutputContext context;

	public Object[] getShowPageContext()
	{
		return new Object[]{context.getPropertyValue().getClass(), context.getPropertyValue()};
	}

/*
// We can't do fancy stuff until http://issues.apache.org/jira/browse/TAP5-829 is fixed
	public TrailsPropertyDescriptor getPropertyDescriptor()
	{
		return descriptorService.getClassDescriptor(beanEditContext.getBeanClass()).getPropertyDescriptor(propertyOutputContext.getPropertyId());
	}
*/

}
