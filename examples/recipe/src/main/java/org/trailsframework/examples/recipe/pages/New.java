package org.trailsframework.examples.recipe.pages;


import org.apache.tapestry5.Link;
import org.trailsframework.hibernate.pages.HibernateNewPage;
import org.trailsframework.util.DisplayNameUtils;

public class New extends HibernateNewPage
{

	public String getTitle()
	{
		return getMessages().format("org.trails.i18n.new", DisplayNameUtils.getDisplayName(getClassDescriptor(), getMessages()));
	}

	public Link back()
	{
		return getResources().createPageLink(List.class, false, getClassDescriptor().getType());
	}
}