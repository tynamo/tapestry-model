package org.tynamo.components;


import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;

public class Identifier
{
	@Parameter(required = true, allowNull = false)
	private Object object;

	@Inject
	private PropertyAccess propertyAccess;

	@Inject
	private DescriptorService descriptorService;

	@BeginRender
	boolean beginRender(MarkupWriter writer)
	{
		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(object.getClass());
		writer.write(propertyAccess.get(object, classDescriptor.getIdentifierDescriptor().getName()).toString());
		return false;
	}
}
