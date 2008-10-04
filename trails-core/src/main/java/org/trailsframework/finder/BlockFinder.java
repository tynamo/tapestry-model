package org.trails.finder;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.util.ComponentAddress;
import org.trails.descriptor.IPropertyDescriptor;

public interface BlockFinder
{

	/**
	 * @return the component address for a block for the specified property
	 */
	public abstract ComponentAddress findBlockAddress(IPropertyDescriptor descriptor);

	/**
	 * Return the block for the specified property
	 *
	 * @param cycle
	 * @param descriptor
	 * @return
	 */
	public Block findBlock(IRequestCycle cycle, IPropertyDescriptor descriptor);

	public ComponentAddress getDefaultBlockAddress();
}