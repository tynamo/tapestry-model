package org.tynamo.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyOutputContext;
import org.tynamo.components.Composition;
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
			"collectionDescriptor=propertyDescriptor", "owner=tynamoBeanContext.bean"})
	private Composition composition;

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
		return descriptorService.getClassDescriptor(tynamoBeanContext.getBeanClass())
				.getPropertyDescriptor(context.getPropertyId());
	}

}
