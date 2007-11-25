package org.trails.component;

import org.apache.tapestry.contrib.table.model.ognl.ExpressionTableColumn;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.trails.descriptor.IPropertyDescriptor;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

public class TrailsTableColumn extends ExpressionTableColumn
{

	protected IPropertyDescriptor propertyDescriptor;

	public TrailsTableColumn(IPropertyDescriptor propertyDescriptor, ExpressionEvaluator evaluator)
	{
		super(propertyDescriptor.getName(), propertyDescriptor.getDisplayName(),
				propertyDescriptor.getName(), true, evaluator);
		this.propertyDescriptor = propertyDescriptor;
	}

	public IPropertyDescriptor getPropertyDescriptor()
	{
		return propertyDescriptor;
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
}
