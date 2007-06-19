package org.trails.page;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.trails.component.Utils;
import org.trails.page.TrailsPage.PageType;

public class DefaultPageResolver implements PageResolver
{
	private String defaultPrefix = "Default";

	private Map<TrailsPage.PageType, String> postFixMap;

	public String getPostFix(TrailsPage.PageType pageType)
	{
		return getPostFixMap().get(pageType);
	}

	public IPage resolvePage(IRequestCycle cycle, String className, PageType pageType)
	{
		if (className == null || "".equals(className)) return cycle.getPage(getDefaultPrefix() + getPostFix(pageType));
		
		String pageName = Utils.unqualify(className) + getPostFix(pageType);
		IPage page = null;
		try
		{
			page = cycle.getPage(pageName);
		} catch (PageNotFoundException ae)
		{

			page = cycle.getPage(getDefaultPrefix() + getPostFix(pageType));

		}
		return page;
	}

	public Map<TrailsPage.PageType, String> getPostFixMap()
	{
		return postFixMap;
	}

	public void setPostFixMap(Map<TrailsPage.PageType, String> postFixMap)
	{
		this.postFixMap = postFixMap;
	}

	public DefaultPageResolver()
	{
		postFixMap = new HashMap<TrailsPage.PageType, String>();
		for (PageType pageType : PageType.values()) postFixMap.put(pageType, pageType.name());
	}

	public String getDefaultPrefix()
	{
		return defaultPrefix;
	}

	public void setDefaultPrefix(String defaultPostfix)
	{
		this.defaultPrefix = defaultPostfix;
	}

}
