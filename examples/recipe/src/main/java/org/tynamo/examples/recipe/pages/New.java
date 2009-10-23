package org.tynamo.examples.recipe.pages;


import org.apache.tapestry5.Link;
import org.tynamo.hibernate.pages.HibernateNewPage;
import org.tynamo.util.DisplayNameUtils;

public class New extends HibernateNewPage
{

	public String getTitle()
	{
		return getMessages().format("org.tynamo.i18n.new", DisplayNameUtils.getDisplayName(getClassDescriptor(), getMessages()));
	}

	public Link back()
	{
		return getPageRenderLinkSource().createPageRenderLinkWithContext(List.class, getClassDescriptor().getType());
	}
}
