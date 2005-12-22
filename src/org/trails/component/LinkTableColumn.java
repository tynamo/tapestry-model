package org.trails.component;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.components.BlockRenderer;
import org.apache.tapestry.components.RenderBlock;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.simple.ITableColumnEvaluator;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.util.ComponentAddress;
import org.trails.descriptor.IPropertyDescriptor;

public class LinkTableColumn extends TrailsTableColumn
{
    private ComponentAddress linkBlockComponentAddress;

    public LinkTableColumn(IPropertyDescriptor propertyDescriptor, 
            ComponentAddress address, 
            ExpressionEvaluator evaluator)
    {
        super(propertyDescriptor, evaluator);
        this.linkBlockComponentAddress = address;
        // TODO Auto-generated constructor stub
    }

    public IRender getValueRenderer(IRequestCycle cycle, ITableModelSource arg1, Object arg2)
    {
        return new BlockRenderer((Block)linkBlockComponentAddress.findComponent(cycle));
    }
    
    

}
