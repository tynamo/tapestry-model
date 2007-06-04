package org.trails.component;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.components.BlockRenderer;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ognl.ExpressionTableColumn;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.util.ComponentAddress;
import org.trails.descriptor.IPropertyDescriptor;

public class TrailsTableColumn extends ExpressionTableColumn
{

	protected IPropertyDescriptor propertyDescriptor;
	protected ComponentAddress blockAddress;

	public TrailsTableColumn(IPropertyDescriptor propertyDescriptor, ExpressionEvaluator evaluator)
	{
		super(propertyDescriptor.getName(), propertyDescriptor.getDisplayName(),
			propertyDescriptor.getName(), true, evaluator);
		this.propertyDescriptor = propertyDescriptor;
	}

	public TrailsTableColumn(IPropertyDescriptor propertyDescriptor,
							 ExpressionEvaluator evaluator,
							 ComponentAddress blockAddress)
	{
		super(propertyDescriptor.getName(), propertyDescriptor.getDisplayName(),
			propertyDescriptor.getName(), true, evaluator);
		this.propertyDescriptor = propertyDescriptor;
		this.blockAddress = blockAddress;
	}

	public IPropertyDescriptor getPropertyDescriptor()
	{
		return propertyDescriptor;
	}

	public void setPropertyDescriptor(IPropertyDescriptor propertyDescriptor)
	{
		this.propertyDescriptor = propertyDescriptor;
	}

	public Object getColumnValue(Object arg0)
	{
		Object value = super.getColumnValue(arg0);
		if (propertyDescriptor.getFormat() != null)
		{
			try
			{
				Format format = null;
				if (propertyDescriptor.isDate())
				{
					format = new SimpleDateFormat(propertyDescriptor.getFormat());
				}
				if (propertyDescriptor.isNumeric())
				{
					format = new DecimalFormat(propertyDescriptor.getFormat());
				}
				return format.format(value);
			} catch (IllegalArgumentException e)
			{
				return value;
			}
		} else
		{
			return value;
		}
	}

	public IRender getValueRenderer(IRequestCycle cycle, ITableModelSource tableModelSource, Object row)
	{
		if (blockAddress != null)
		{
			return new BlockRenderer((Block) blockAddress.findComponent(cycle));
		} else
		{
			return super.getValueRenderer(cycle, tableModelSource, row);
		}
	}

	ComponentAddress getBlockAddress()
	{
		return blockAddress;
	}


}
