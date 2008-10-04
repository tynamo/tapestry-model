package org.trails.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class DefaultPageResolver implements PageResolver
{
	private static final Log LOG = LogFactory.getLog(DefaultPageResolver.class);

	private String defaultPrefix = "Default";

	private boolean cacheDisabled = System.getProperty("org.apache.tapestry.disable-caching") != null;

	private Map<PageType, String> postFixMap;

	public String getPostFix(PageType pageType)
	{
		return getPostFixMap().get(pageType);
	}

	public IPage resolvePage(IRequestCycle cycle, Class type, PageType pageType)
	{
		Defense.notNull(type, "type");
		Defense.notNull(pageType, "pageType");

		String pageName = type.getSimpleName() + getPostFix(pageType);
		IPage page = null;
		try
		{
			page = cycle.getPage(pageName);

		} catch (PageNotFoundException ae)
		{
			page = cycle.getPage(getDefaultPrefix() + getPostFix(pageType));

			if (!cacheDisabled)
			{
				if (LOG.isDebugEnabled())
					LOG.debug(_formatter.format("installing-page", pageName, page.getNamespace(), page.getSpecification()));
				page.getNamespace().installPageSpecification(pageName, page.getSpecification());
				page = cycle.getPage(pageName);
			}
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

	public void setCacheDisabled(boolean cacheDisabled)
	{
		this.cacheDisabled = cacheDisabled;
	}

	private static final MessageFormatter _formatter = new MessageFormatter(LOG, "org.apache.tapestry.resolver.ResolverStrings");
}
