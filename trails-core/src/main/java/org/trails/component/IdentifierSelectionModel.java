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

import java.util.List;

import ognl.Ognl;
import org.apache.commons.lang.StringUtils;
import org.trails.TrailsRuntimeException;


public class IdentifierSelectionModel extends AbstractPropertySelectionModel
{
	private String idProperty = "id";

	public IdentifierSelectionModel(List instances, String idProperty)
	{
		super(instances);
		this.idProperty = idProperty;
	}

	public IdentifierSelectionModel(List instances, String idProperty, boolean allowNone)
	{
		super(instances, allowNone);
		this.idProperty = idProperty;
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
				return Ognl.getValue(idProperty,instances.get(index)).toString();
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


	public boolean isDisabled(int i)
	{
		return false; 
	}
}
