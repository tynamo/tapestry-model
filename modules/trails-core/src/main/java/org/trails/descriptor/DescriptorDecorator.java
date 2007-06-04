package org.trails.descriptor;

/**
 * @author Chris Nelson
 *         <p/>
 *         A descriptor decorator may modify or replace an IClassDescriptor.  A
 *         "pipeline" of these is reponsible for ultimately building the final descriptors.
 */
public interface DescriptorDecorator
{
	public IClassDescriptor decorate(IClassDescriptor descriptor);
}
