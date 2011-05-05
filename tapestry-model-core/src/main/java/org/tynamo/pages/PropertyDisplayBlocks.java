package org.tynamo.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyOutputContext;
import org.tynamo.PageType;
import org.tynamo.components.Download;
import org.tynamo.components.internal.Composition;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.TynamoBeanContext;
import org.tynamo.services.TynamoPageRenderLinkSource;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;

public class PropertyDisplayBlocks
{
	@Inject
	private DescriptorService descriptorService;

	@Inject
	private TynamoPageRenderLinkSource tynamoPageRenderLinkSource;

	@Inject
	private Locale locale;

	private final DateFormat dateFormat = DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, locale);

	private final NumberFormat numberFormat = NumberFormat.getInstance(locale);

	@Environmental
	@Property(write = false)
	private PropertyOutputContext context;

	@Environmental(false)
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

	@Inject
	@Property
	private Block missingAdvisor;

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

	public DateFormat getDateFormat()
	{
		String format = getPropertyDescriptor().getFormat();
		return format != null ? new SimpleDateFormat(format) : dateFormat;
	}

	public NumberFormat getNumberFormat()
	{
		String format = getPropertyDescriptor().getFormat();
		return format != null ? new DecimalFormat(format) : numberFormat;
	}

	public String getShow()
	{
		return tynamoPageRenderLinkSource.getCanonicalPageName(PageType.SHOW);
	}

}
