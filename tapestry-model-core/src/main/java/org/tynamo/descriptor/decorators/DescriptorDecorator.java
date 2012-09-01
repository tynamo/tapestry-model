package org.tynamo.descriptor.decorators;

import org.tynamo.descriptor.TynamoClassDescriptor;

/**
 *         A descriptor decorator may modify or replace an TynamoClassDescriptor.  A
 *         "pipeline" of these is responsible for ultimately building the final descriptors.
 */
public interface DescriptorDecorator
{
	public TynamoClassDescriptor decorate(TynamoClassDescriptor descriptor);
}
