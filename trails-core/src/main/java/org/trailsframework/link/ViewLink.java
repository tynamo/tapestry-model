package org.trails.link;

import org.trails.page.PageType;
import org.apache.tapestry.annotations.ComponentClass;

/**
 * This component displays a link to the ViewPage for an object
 */
@ComponentClass
public abstract class ViewLink extends ModelLink
{
	public PageType getPageType()
	{
		return PageType.VIEW;
	}
}