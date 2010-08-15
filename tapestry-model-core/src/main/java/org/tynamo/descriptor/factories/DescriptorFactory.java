package org.tynamo.descriptor.factories;

import org.tynamo.descriptor.TynamoClassDescriptor;

public interface DescriptorFactory
{

	public TynamoClassDescriptor buildClassDescriptor(Class type);

}
