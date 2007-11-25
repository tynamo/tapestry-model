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
package org.trails.page;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.trails.component.TrailsTableColumn;

import java.util.List;

/**
 * List all the instances of a type
 *
 * @author Chris Nelson
 */
public abstract class ListPage extends TrailsPage
{

	public abstract Object getObject();

	public abstract void setObject(Object object);

	public abstract List getInstances();

	public abstract void setInstances(List Instances);

	public abstract ITableColumn getColumn();

	public abstract void setColumn(ITableColumn column);

	public void activateTrailsPage(Object[] objects, IRequestCycle iRequestCycle)
	{
		if (getCallbackStack() != null)
		{
			getCallbackStack().clear();
		}
	}
}
