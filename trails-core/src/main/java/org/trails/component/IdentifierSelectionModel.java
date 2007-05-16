/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.component;

import java.util.ArrayList;
import java.util.List;

import ognl.Ognl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.TrailsRuntimeException;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class IdentifierSelectionModel implements IPropertySelectionModel
{
	protected List instances;
	private String idProperty = "id";
	protected boolean allowNone;

	protected String labelProperty = "toString()";

	public static String DEFAULT_NONE_LABEL = "None";
	public static String DEFAULT_NONE_VALUE = "none";

	protected String noneLabel = DEFAULT_NONE_LABEL;

	public IdentifierSelectionModel()
	{
	}

	public IdentifierSelectionModel(List instances, String idProperty)
	{
		this.idProperty = idProperty;

		this.instances = instances;
	}

	public IdentifierSelectionModel(List instances, boolean allowNone)
	{
		setAllowNone(instances, allowNone);
	}

	protected void setAllowNone(List instances, boolean allowNone)
	{
		this.allowNone = allowNone;
		this.instances = new ArrayList();
		this.instances.addAll(instances);
		if (this.allowNone)
		{
			this.instances.add(0, null);
		}
	}

	public IdentifierSelectionModel(List instances, String idProperty, boolean allowNone)
	{
		this(instances, allowNone);
		this.idProperty = idProperty;
	}

	/* (non-Javadoc)
		 * @see org.apache.tapestry.form.IPropertySelectionModel#getOptionCount()
		 */
	public int getOptionCount()
	{
		// TODO Auto-generated method stub
		return instances.size();
	}

	/* (non-Javadoc)
		 * @see org.apache.tapestry.form.IPropertySelectionModel#getOption(int)
		 */
	public Object getOption(int index)
	{
		return instances.get(index);
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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
				return BeanUtils.getProperty(instances.get(index), idProperty);
			}
		} catch (Exception e)
		{
			throw new TrailsRuntimeException(e);
		}
	}

	/* (non-Javadoc)
		 * @see org.apache.tapestry.form.IPropertySelectionModel#translateValue(java.lang.String)
		 */
	public Object translateValue(String value)
	{
		if (StringUtils.isEmpty(value)) return null;
		List realInstances = allowNone ? instances.subList(1, instances.size()) : instances;
		try
		{
			if (allowNone)
			{
				if (value.equals(DEFAULT_NONE_VALUE)) return null;
			}
			List matches = (List) Ognl.getValue(
				"#root.{? #this." + idProperty + ".toString() == \"" + value + "\" }",
				realInstances);
			if (matches.size() > 0) return matches.get(0);
			return null;
		} catch (Exception e)
		{
			throw new TrailsRuntimeException(e);
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
}
