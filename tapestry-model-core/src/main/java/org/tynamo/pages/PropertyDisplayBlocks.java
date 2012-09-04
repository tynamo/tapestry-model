package org.tynamo.pages;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyOutputContext;
import org.tynamo.components.Download;
import org.tynamo.components.internal.Composition;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.TynamoBeanContext;

public class PropertyDisplayBlocks
{
	@Inject
	private DescriptorService descriptorService;

	@Inject
	private Locale locale;

	@Environmental
	@Property(write = false)
	private PropertyOutputContext context;

	@Environmental(false)
	@Property(write = false)
	private TynamoBeanContext tynamoBeanContext;

	@Component
	private Composition composition;

	@Component(parameters = {"model=tynamoBeanContext.beanInstance", "propertyDescriptor=propertyDescriptor"})
	private Download download;

	@Inject
	@Property
	private Block missingAdvisor;

	public TynamoPropertyDescriptor getPropertyDescriptor()
	{
		return descriptorService.getClassDescriptor(tynamoBeanContext.getBeanType())
				.getPropertyDescriptor(context.getPropertyId());
	}

	public DateFormat getDateFormat()
	{
		String format = getPropertyDescriptor().getFormat();
		return format != null ? new SimpleDateFormat(format) : DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, locale);
	}

	public NumberFormat getNumberFormat()
	{
		String format = getPropertyDescriptor().getFormat();
		return format != null ? new DecimalFormat(format) : NumberFormat.getInstance(locale);
	}

}
