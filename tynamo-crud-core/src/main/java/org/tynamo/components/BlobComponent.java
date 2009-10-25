package org.tynamo.components;

import org.apache.tapestry5.annotations.Parameter;
import org.tynamo.descriptor.annotation.PropertyDescriptor;

public class BlobComponent
{
	@Parameter(required = true)
	private Object model;

	@Parameter(required = true)
	private PropertyDescriptor descriptor;

	@Parameter
	private byte[] bytes;

}