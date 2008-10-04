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
package org.trails.link;

import org.apache.tapestry.annotations.ComponentClass;
import org.trails.page.PageType;

/**
 * ListAllLink renders a single link that has a target of the List page for a given object.  It is abstract because
 * Tapestry will populate the bean with getter/setters for fields in it.  The base class is constructed with the name
 * of the type that this link will connect to, and all list operations are synthesized from that single name.
 */
@ComponentClass
public abstract class ListAllLink extends AbstractTypeNavigationLink
{

	public PageType getPageType()
	{
		return PageType.LIST;
	}

	public String getBundleKey()
	{
		return "org.trails.component.listalllink";
	}

	public String getDefaultMessage()
	{
		return "[TRAILS][ORG.TRAILS.COMPONENT.LISTALLLINK]";
	}

	public Object getParams()
	{
		return getClassDescriptor().getPluralDisplayName();
	}
}
