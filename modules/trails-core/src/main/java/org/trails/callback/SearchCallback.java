package org.trails.callback;

import org.apache.tapestry.IRequestCycle;
import org.trails.page.SearchPage;

/**
 * @author Chris Nelson
 *         <p/>
 *         Return Control to a search page.
 */
public class SearchCallback extends TrailsCallback
{

	private String typeName;

	public SearchCallback(String name, String typeName)
	{
		super(name);
		this.typeName = typeName;
	}

	public SearchCallback(String name)
	{
		super(name);
	}

	public void performCallback(IRequestCycle cycle)
	{
		SearchPage searchPage = (SearchPage) cycle.getPage(getPageName());
		searchPage.setTypeName(typeName);
		cycle.activate(searchPage);
	}

}
