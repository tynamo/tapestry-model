package org.trails.page;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.trails.page.PageType;

public class DefaultPageResolver implements PageResolver
{
	private String defaultPrefix = "Default";

	private Map<PageType, String> postFixMap;

	public String getPostFix(PageType pageType)
	{
		return getPostFixMap().get(pageType);
	}

	public IPage resolvePage(IRequestCycle cycle, Class type, PageType pageType)
	{
		if (type == null) return cycle.getPage(getDefaultPrefix() + getPostFix(pageType));

		String pageName = type.getSimpleName() + getPostFix(pageType);
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

	public Map<PageType, String> getPostFixMap()
	{
		return postFixMap;
	}

	public void setPostFixMap(Map<PageType, String> postFixMap)
	{
		this.postFixMap = postFixMap;
	}

	public DefaultPageResolver()
	{
		postFixMap = new HashMap<PageType, String>();
		for (PageType pageType : PageType.values())
		{
			postFixMap.put(pageType, pageType.toString());
		}
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
