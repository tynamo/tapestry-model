/*
 * Created on Feb 28, 2005
 *
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
package org.trails.callback;

import org.apache.tapestry.IRequestCycle;
import org.trails.page.ListPage;

/**
 * Returns control to a ListPage
 *
 * @author Chris Nelson
 */
public class ListCallback extends TrailsCallback
{

	private String typeName;

	private Class clazz;

	public ListCallback(String pageName, String typeName, Class clazz)
	{
		super(pageName);
		this.typeName = typeName;
		this.clazz = clazz;
	}

	/**
	 * (non-Javadoc)T
	 *
	 * @see org.apache.tapestry.callback.ICallback#performCallback(org.apache.tapestry.IRequestCycle)
	 */
	public void performCallback(IRequestCycle cycle)
	{
		ListPage listPage = (ListPage) cycle.getPage(getPageName());
		preparePageForCycleActivate(listPage);
		cycle.activate(listPage);
	}

	protected void preparePageForCycleActivate(ListPage listPage)
	{
		listPage.setTypeName(typeName);
		listPage.setType(clazz);
//        listPage.reloadInstances(); //@todo: mmmm do we really need to reloadtheInstances?
	}
}
