package org.tynamo.examples.simple.pages;


import org.apache.tapestry5.Link;
import org.tynamo.hibernate.pages.HibernateNewPage;
import org.tynamo.util.DisplayNameUtils;

public class Add extends HibernateNewPage
{

	public String getTitle()
	{
		return getMessages().format("org.tynamo.i18n.edit", DisplayNameUtils.getDisplayName(getClassDescriptor(), getMessages()));
	}

	public Link back()
	{
		return getPageRenderLinkSource().createPageRenderLinkWithContext(List.class, getClassDescriptor().getType());
	}
}
