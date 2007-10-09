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

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.trails.page.PageType;

/**
 * ListAllLink renders a single link that has a target of the List page for a given object.  It is abstract because
 * Tapestry will populate the bean with getter/setters for fields in it.  The base class is constructed with the name
 * of the type that this link will connect to, and all list operations are synthesized from that single name.
 *
 * @author fus8882
 * @date Sep 26, 2004
 */
public abstract class ListAllLink extends AbstractTypeNavigationLink
{

	/**
	 * Get the text for the rendered link
	 *
	 * @return Full i18n text in the form "List Foobars"
	 */
	public String getLinkText()
	{
		String name = getClassDescriptor().getPluralDisplayName();
		return generateLinkText(name, "org.trails.component.listalllink", "[TRAILS][ORG.TRAILS.COMPONENT.LISTALLLINK]");
	}

	/**
	 * For the page type PageType.LIST, try to get the name of the page.  Called by ListAllLink.jwc
	 *
	 * @return Name of the page
	 */
	public String getListPageName()
	{
		IRequestCycle cycle = getPage().getRequestCycle();
		IPage iPage = getPageResolver().resolvePage(cycle, getType(), PageType.List);

		return iPage.getPageName();
	}
}
