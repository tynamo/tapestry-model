package org.trails.link;

import org.trails.page.PageType;

/**
 * This component displays a link to the ViewPage for an object
 */
public abstract class ViewLink extends ModelLink
{
	public PageType getPageType()
	{
		return PageType.VIEW;
	}
}