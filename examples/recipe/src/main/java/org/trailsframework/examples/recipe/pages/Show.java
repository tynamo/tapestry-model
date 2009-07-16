package org.trailsframework.examples.recipe.pages;


import org.apache.tapestry5.Link;
import org.trailsframework.hibernate.pages.HibernateModelPage;

public class Show extends HibernateModelPage
{

	public Object[] getEditPageContext()
	{
		return new Object[]{getClassDescriptor().getType(), getBean()};
	}

	public Link back()
	{
		return getResources().createPageLink(List.class, false, getClassDescriptor().getType());
	}

}