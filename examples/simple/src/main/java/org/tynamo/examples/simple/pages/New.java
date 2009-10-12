package org.tynamo.examples.simple.pages;


import org.apache.tapestry5.Link;
import org.tynamo.hibernate.pages.HibernateNewPage;
import org.tynamo.util.DisplayNameUtils;

public class New extends HibernateNewPage
{

	public String getTitle()
	{
		return getMessages().format("org.tynamo.i18n.edit", DisplayNameUtils.getDisplayName(getClassDescriptor(), getMessages()));
	}

	public Link back()
	{
		return getResources().createPageLink(List.class, false, getClassDescriptor().getType());
	}
}