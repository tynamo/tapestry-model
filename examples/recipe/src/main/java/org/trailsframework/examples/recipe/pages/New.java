package org.trailsframework.examples.recipe.pages;


import org.apache.tapestry5.Link;
import org.trailsframework.hibernate.pages.HibernateNewPage;

public class New extends HibernateNewPage
{


	public Link back()
	{
		return getResources().createPageLink(List.class, false, getClassDescriptor().getType());
	}
}