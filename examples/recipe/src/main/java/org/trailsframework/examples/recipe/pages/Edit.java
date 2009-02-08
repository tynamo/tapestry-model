package org.trailsframework.examples.recipe.pages;


import org.apache.tapestry5.Link;
import org.trailsframework.hibernate.pages.HibernateEditPage;

public class Edit extends HibernateEditPage
{

	public Link back()
	{
		return getResources().createPageLink(List.class, false, getClassDescriptor().getType());
	}
}
