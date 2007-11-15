package org.trails.page;

import org.apache.commons.lang.StringUtils;

public enum PageType
{
	SEARCH, NEW, EDIT, LIST, VIEW, EXCEPTION;

	public String toString()
	{
		return StringUtils.capitalize(name().toLowerCase());
	}
}
