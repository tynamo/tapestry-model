package org.trailsframework.pages;

import org.trailsframework.descriptor.TrailsPropertyDescriptor;
import org.trailsframework.util.Identifiable;
import org.apache.tapestry5.services.PropertyOutputContext;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;

import java.util.Set;
import java.util.Collection;

public class PropertyDisplayBlocks
{

	@Environmental
	@Property(write = false)
	private PropertyOutputContext context;

	@Property
	private Identifiable loopIterator;

	@Property
	private int loopIndex;


	public Object[] getShowPageContext()
	{
		return new Object[]{context.getPropertyValue().getClass(), context.getPropertyValue()};
	}

	public Object[] getLoopShowPageContext()
	{
		return new Object[]{loopIterator.getClass(), loopIterator};
	}

	public boolean isLastElement()
	{
		return loopIndex >= ((Collection) context.getPropertyValue()).size() - 1;
	}

/*
// We can't do fancy stuff until http://issues.apache.org/jira/browse/TAP5-829 is fixed
	public TrailsPropertyDescriptor getPropertyDescriptor()
	{
		return descriptorService.getClassDescriptor(beanEditContext.getBeanClass()).getPropertyDescriptor(propertyOutputContext.getPropertyId());
	}
*/

}
