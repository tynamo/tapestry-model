package org.trails.callback;

import org.apache.tapestry.IRequestCycle;
import org.trails.page.SearchPage;

/**
 * Return Control to a search page.
 *
 * @author Chris Nelson
 */
public class SearchCallback extends TrailsCallback
{

	private Class type;

	public SearchCallback(String name, Class type)
	{
		super(name);
		this.type = type;
	}

	public SearchCallback(String name)
	{
		super(name);
	}

	public void performCallback(IRequestCycle cycle)
	{
		SearchPage searchPage = (SearchPage) cycle.getPage(getPageName());
		searchPage.setType(type);
		cycle.activate(searchPage);
	}

}
