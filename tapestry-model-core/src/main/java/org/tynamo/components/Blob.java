package org.tynamo.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.tynamo.descriptor.TynamoPropertyDescriptor;

public class Blob
{
	@Parameter(required = true)
	@Property(write = false)
	private Object model;

	@Parameter(required = true)
	@Property(write = false)
	private TynamoPropertyDescriptor propertyDescriptor;

}