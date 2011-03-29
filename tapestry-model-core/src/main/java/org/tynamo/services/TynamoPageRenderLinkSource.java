package org.tynamo.services;


import org.apache.tapestry5.Link;
import org.tynamo.PageType;

public interface TynamoPageRenderLinkSource
{
	String getCanonicalPageName(PageType type);

	Link createPageRenderLinkWithContext(PageType pageType, Object... context);
}
