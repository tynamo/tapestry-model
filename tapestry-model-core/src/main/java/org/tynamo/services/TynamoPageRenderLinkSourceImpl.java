package org.tynamo.services;


import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.tynamo.PageType;

import java.util.HashMap;
import java.util.Map;

public class TynamoPageRenderLinkSourceImpl implements TynamoPageRenderLinkSource
{

	private PageRenderLinkSource pageRenderLinkSource;
	private Map<PageType, String> pageMap;

	public TynamoPageRenderLinkSourceImpl(PageRenderLinkSource pageRenderLinkSource,
	                                      ComponentClassResolver componentClassResolver,
	                                      Map<PageType, Class> pageMap)
	{
		this.pageRenderLinkSource = pageRenderLinkSource;
		this.pageMap = new HashMap<PageType, String>();

		for (Map.Entry<PageType, Class> entry : pageMap.entrySet())
		{
			String pageClassName = entry.getValue().getName();
			String canonicalPageName = componentClassResolver
					.canonicalizePageName(componentClassResolver.resolvePageClassNameToPageName(pageClassName));
			this.pageMap.put(entry.getKey(), canonicalPageName);
		}

	}

	public Link createPageRenderLinkWithContext(PageType pageType, Object... context)
	{
		return pageRenderLinkSource.createPageRenderLinkWithContext(pageMap.get(pageType), context);
	}

	public String getCanonicalPageName(PageType type)
	{
		return pageMap.get(type);
	}
}
