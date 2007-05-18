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
import org.hibernate.criterion.DetachedCriteria;
import org.trails.page.HibernateListPage;

/**
 * @author Chris Nelson
 *         <p/>
 *         Returns control to a ListPage
 */
public class HibernateListCallback extends TrailsCallback
{
	private String typeName;

	private DetachedCriteria criteria;

	public HibernateListCallback(String pageName, String typeName, DetachedCriteria criteria)
	{
		super(pageName);
		this.typeName = typeName;
		this.criteria = criteria;
	}

	/* (non-Javadoc)T
		 * @see org.apache.tapestry.callback.ICallback#performCallback(org.apache.tapestry.IRequestCycle)
		 */
	public void performCallback(IRequestCycle cycle)
	{
		HibernateListPage listPage = (HibernateListPage) cycle.getPage(getPageName());
		listPage.setTypeName(typeName);
		listPage.setCriteria(getCriteria());
		cycle.activate(listPage);
	}

	/**
	 * @return Returns the typeName.
	 */
	public String getTypeName()
	{
		return typeName;
	}

	/**
	 * @param typeName The typeName to set.
	 */
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public DetachedCriteria getCriteria()
	{
		return criteria;
	}

	public void setCriteria(DetachedCriteria criteria)
	{
		this.criteria = criteria;
	}


}