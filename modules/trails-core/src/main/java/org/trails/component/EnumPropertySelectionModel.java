package org.trails.component;

import java.util.Arrays;
import java.util.List;

import org.trails.TrailsRuntimeException;

/**
 * Implementation of a property model that works off of native
 * java enum types.

 * <p>
 * The main diference with Tapestry's EnumPropertySelectionModel is that "allowNone" and the property used to get the
 * label values (labelProperty) are both configurable.
 * </p>

 * <p>
 * The enum label/values are all translated by calling the method indicated by "labelProperty"
 * By default labelProperty = "toString()", so it may be a good idea to provide a toString() method in your Enums if the
 * types aren't what you would prefer to display.
 * </p>
 */
public class EnumPropertySelectionModel extends AbstractPropertySelectionModel
{
	private Class type;

	public EnumPropertySelectionModel(Class type)
	{
		this(type, false);

	}

	public EnumPropertySelectionModel(List instances, boolean allowNone)
	{
		super(instances, allowNone);
	}

	public EnumPropertySelectionModel(Class type, boolean allowNone)
	{
		super(Arrays.asList(type.getEnumConstants()), allowNone);
		this.type = type;
	}

	public EnumPropertySelectionModel(Class type, String labelProperty, boolean allowNone)
	{
		this(type, allowNone);
		setLabelProperty(labelProperty);
	}

	/*
		 * (non-Javadoc)
		 *
		 * @see org.apache.tapestry.form.IPropertySelectionModel#getValue(int)
		 */
	public String getValue(int index)
	{
		try
		{
			if (allowNone && index == 0)
			{
				return DEFAULT_NONE_VALUE;
			} else
			{
				return instances.get(index).toString();
			}
		} catch (Exception e)
		{
			throw new TrailsRuntimeException(e, type);
		}
	}

	/*
		 * (non-Javadoc)
		 *
		 * @see org.apache.tapestry.form.IPropertySelectionModel#translateValue(java.lang.String)
		 */
	public Object translateValue(String value)
	{
		if (allowNone)
		{
			if (value.equals(DEFAULT_NONE_VALUE))
				return null;
		}
		for (Object enumElement : instances)
		{
			if (enumElement != null && enumElement.toString().equals(value))
			{
				return enumElement;
			}
		}
		return null;
	}
}
