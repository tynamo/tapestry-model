package org.trails.finder;

import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.util.ComponentAddress;
import org.trails.page.IEditorBlockPage;
import org.trails.descriptor.IPropertyDescriptor;

public class BaseBlockFinder implements BlockFinder
{

	private Map<String, ComponentAddress> editorMap;
	private ComponentAddress defaultEditor;

	public Map<String, ComponentAddress> getEditorMap()
	{
		return editorMap;
	}

	/**
	 * This a map where the keys are ognl expressions and the values are component address.
	 *
	 * @param editorMap
	 */
	public void setEditorMap(Map<String, ComponentAddress> editorMap)
	{
		this.editorMap = editorMap;
	}

	/**
	 * @param descriptor
	 * @return The first component address in the editorMap whose key evaluates to true for descriptor. This will be used
	 *         to load an editor for the descriptor. Returns default editor if no match is found.
	 * @see BlockFinder#findBlockAddress(org.trails.descriptor.IPropertyDescriptor)
	 */
	public ComponentAddress findBlockAddress(IPropertyDescriptor descriptor)
	{
		ComponentAddress componentAddress = findBlockAddress(editorMap, descriptor);
		return componentAddress != null ? componentAddress : getDefaultBlockAddress();
	}

	protected ComponentAddress findBlockAddress(Map<String, ComponentAddress> map, IPropertyDescriptor descriptor)
	{
		for (Map.Entry<String, ComponentAddress> entry : map.entrySet())
		{
			try
			{
				if ((Boolean) Ognl.getValue(entry.getKey(), descriptor))
				{
					return entry.getValue();
				}
			} catch (OgnlException e)
			{
			}
		}
		return null;
	}

	public Block findBlock(IRequestCycle cycle, IPropertyDescriptor descriptor)
	{
		if (cycle.getPage().getComponents().containsKey(descriptor.getName()))
		{
			Block block = (Block) cycle.getPage().getComponent(descriptor.getName());
			return block;
		} else
		{
			// since it came from a block container page, we need to set
			// the model and descriptor on the container page so its visible to the
			// block
			ComponentAddress blockAddress = findBlockAddress(descriptor);
			Block block = (Block) blockAddress.findComponent(cycle);
			((IEditorBlockPage) block.getPage()).setDescriptor(descriptor);
			return block;
		}
	}

	public ComponentAddress getDefaultBlockAddress()
	{
		return defaultEditor;
	}

	public void setDefaultBlockAddress(ComponentAddress defaultEditor)
	{
		this.defaultEditor = defaultEditor;
	}

}
