package org.trails.descriptor;

import java.util.Iterator;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.util.ComponentAddress;

public class BaseBlockFinder implements BlockFinder
{

	private Map editorMap;
	private ComponentAddress defaultEditor;

	public Map getEditorMap()
	{
	    return editorMap;
	}

	/**
	 * This a map where the keys are ognl expressions and the values are
	 * component address.
	 * 
	 * @param editorMap
	 */
	public void setEditorMap(Map editorMap)
	{
	    //System.out.println("map type: " + editorMap.getClass().getName());
	    this.editorMap = editorMap;
	}

	/**
	 * @param descriptor
	 * @return The first component address in the editorMap whose key evaluates
	 *         to true for descriptor. This will be used to load an editor for
	 *         the descriptor. Returns default editor if no match is found.
	 * @see org.trails.descriptor.BlockFinder#findBlockAdress(org.trails.descriptor.IPropertyDescriptor)
	 */
	public ComponentAddress findBlockAddress(IPropertyDescriptor descriptor)
	{
	    for (Iterator iter = editorMap.entrySet().iterator(); iter.hasNext();)
	    {
	        Map.Entry entry = (Map.Entry) iter.next();
	        try
	        {
	            if (((Boolean) Ognl.getValue((String) entry.getKey(),
	                    descriptor)).booleanValue())
	            {
	                return (ComponentAddress) entry.getValue();
	            }
	        } catch (OgnlException e)
	        {
	            // TODO Auto-generated catch block
	            //e.printStackTrace();
	        }
	    }
	    return getDefaultBlockAddress();
	}

	public Block findBlock(IRequestCycle cycle, IPropertyDescriptor descriptor)
	{
		if (cycle.getPage().getComponents().containsKey(descriptor.getName()))
		{
			Block block =  (Block)cycle.getPage().getComponent(descriptor.getName());
			return block;
		}
		else
		{
			// since it came from a block container page, we need to set
			// the model and descriptor on the container page so its visible to the
			// block
			ComponentAddress blockAddress = findBlockAddress(descriptor);
			Block block = (Block)blockAddress.findComponent(cycle);
	        block.getPage().setProperty("descriptor", descriptor);
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
