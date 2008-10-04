package org.trails.link;

import org.apache.tapestry.annotations.ComponentClass;
import org.trails.page.PageType;

/**
 * Finds the search page for the type specified by the typeName
 * component parameter and forwards to it, setting the example model
 * to a new instance.  This instance is use to build a query by
 * example.
 */
@ComponentClass
public abstract class SearchLink extends AbstractTypeNavigationLink
{

	public PageType getPageType()
	{
		return PageType.SEARCH;
	}

	public String getBundleKey()
	{
		return "org.trails.component.searchlink";
	}

	public String getDefaultMessage()
	{
		return "[TRAILS][ORG.TRAILS.COMPONENT.SEARCHLINK]";
	}

	public Object getParams()
	{
		return getClassDescriptor().getPluralDisplayName();
	}
}
