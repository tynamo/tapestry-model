package org.trails.component;

import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.util.ComponentAddress;
import org.trails.descriptor.IPropertyDescriptor;

public class LinkTableColumn extends TrailsTableColumn
{
    public LinkTableColumn(IPropertyDescriptor propertyDescriptor, 
            ComponentAddress address, 
            ExpressionEvaluator evaluator)
    {
        super(propertyDescriptor, evaluator);
        this.blockAddress = address;
        // TODO Auto-generated constructor stub
    }
    
    

}
