package org.trails.callback;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;

public class TrailsCallback implements ICallback
{
	private String pageName;

	public TrailsCallback(String pageName)
	{
		this.pageName = pageName;
	}

	public void performCallback(IRequestCycle cycle)
	{
		Defense.notNull(cycle, "cycle");
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

	public boolean shouldReplace(ICallback callback)
	{
		return this.equals(callback);
	}

	@Override
	public boolean equals(Object obj)
	{
		return EqualsBuilder.reflectionEquals(this, obj);
	}


}