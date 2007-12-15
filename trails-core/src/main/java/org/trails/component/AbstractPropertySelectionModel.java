package org.trails.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.exception.TrailsRuntimeException;
import ognl.Ognl;

public abstract class AbstractPropertySelectionModel implements IPropertySelectionModel
{
	protected List instances;
	protected boolean allowNone;
	protected String labelProperty = "toString()";
	public static final String DEFAULT_NONE_LABEL = "None";
	public static final String DEFAULT_NONE_VALUE = "none";
	protected String noneLabel = DEFAULT_NONE_LABEL;

	public AbstractPropertySelectionModel(List instances)
	{
		this(instances, false);
	}

	public AbstractPropertySelectionModel(List instances, boolean allowNone)
	{
		this.allowNone = allowNone;
		this.instances = new ArrayList();
		this.instances.addAll(instances);
		if (this.allowNone)
		{
			this.instances.add(0, null);
		}
	}

	public String getNoneLabel()
	{
		return noneLabel;
	}

	public void setNoneLabel(String noneLabel)
	{
		this.noneLabel = noneLabel;
	}

	public String getLabelProperty()
	{
		return labelProperty;
	}

	public void setLabelProperty(String labelProperty)
	{
		this.labelProperty = labelProperty;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.apache.tapestry.form.IPropertySelectionModel#getOptionCount()
	 */
	public int getOptionCount()
	{
		return instances.size();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.apache.tapestry.form.IPropertySelectionModel#getOption(int)
	 */
	public Object getOption(int index)
	{
		return instances.get(index);
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see org.apache.tapestry.form.IPropertySelectionModel#getLabel(int)
	 */
	public String getLabel(int index)
	{
		if (allowNone && index == 0)
		{
			return getNoneLabel();
		}
		try
		{
			return Ognl.getValue(labelProperty, instances.get(index)).toString();
		} catch (Exception e)
		{
			throw new TrailsRuntimeException(e);
		}
	}

	public boolean isDisabled(int i)
	{
		return false;
	}
}
