package org.trails.component;

import org.trails.page.PageType;

/**
 * This component displays a link to the ViewPage for an object
 */
public abstract class ViewLink extends AbstractModelNavigationLink
{
	public PageType getPageType()
	{
		return PageType.View;
	}
}