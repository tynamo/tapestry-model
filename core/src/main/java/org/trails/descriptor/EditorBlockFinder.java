package org.trails.descriptor;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;




public class EditorBlockFinder extends BaseBlockFinder
{
    public EditorBlockFinder()
    {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	public Block findBlock(IRequestCycle cycle, IPropertyDescriptor descriptor)
	{
		
		Block block = super.findBlock(cycle, descriptor);
		block.getPage().setProperty("model", cycle.getPage().getProperty("model"));
        return block;
	}

    
}
