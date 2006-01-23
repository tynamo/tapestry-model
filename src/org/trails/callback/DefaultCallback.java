package org.trails.callback;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;

public class DefaultCallback implements ICallback
{
	private String pageName;

	public DefaultCallback(String pageName)
	{
		this.pageName = pageName;
	}

	public void performCallback(IRequestCycle cycle)
	{
		cycle.activate(cycle.getPage(getPageName()));

	}

	public String getPageName()
	{
		return pageName;
	}

	public void setPageName(String pageName)
	{
		this.pageName = pageName;
	}
}