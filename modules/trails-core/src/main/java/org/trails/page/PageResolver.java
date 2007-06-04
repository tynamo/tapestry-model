package org.trails.page;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.trails.page.TrailsPage.PageType;

public interface PageResolver
{

	public IPage resolvePage(IRequestCycle cycle, String className, PageType edit);

}
