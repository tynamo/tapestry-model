package org.tynamo.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyOutputContext;
import org.chenillekit.tapestry.core.components.DateFormat;
import org.tynamo.components.Download;
import org.tynamo.components.internal.Composition;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.TynamoBeanContext;

import java.util.Collection;

public class PropertyDisplayBlocks
{
	@Inject
	private DescriptorService descriptorService;

	@Environmental
	@Property(write = false)
	private PropertyOutputContext context;

	@Environmental
	@Property(write = false)
	private TynamoBeanContext tynamoBeanContext;

	@Property
	private Object loopIterator;

	@Property
	private int loopIndex;

	@Component(parameters = {"collection=context.propertyValue", "clientId=prop:context.propertyId",
			"property=propertyDescriptor.name", "owner=tynamoBeanContext.beanInstance"})
	private Composition composition;

	@Component(parameters = {"model=tynamoBeanContext.beanInstance", "propertyDescriptor=propertyDescriptor"})
	private Download download;

	@Component(
			parameters = {"value=context.propertyValue", "clientId=prop:context.propertyid",
			              "pattern=prop:propertyDescriptor.format"})
	private DateFormat date;


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

	public TynamoPropertyDescriptor getPropertyDescriptor()
	{
		return descriptorService.getClassDescriptor(tynamoBeanContext.getBeanType())
				.getPropertyDescriptor(context.getPropertyId());
	}

}
