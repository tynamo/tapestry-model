package org.tynamo.examples.recipe.pages;


import org.apache.tapestry5.Link;
import org.tynamo.hibernate.pages.HibernateEditPage;
import org.tynamo.util.DisplayNameUtils;

public class Edit extends HibernateEditPage
{
	public String getTitle()
	{
		return getMessages().format("org.tynamo.i18n.edit", DisplayNameUtils.getDisplayName(getClassDescriptor(), getMessages()));
	}


	public Link back()
	{
		return getPageRenderLinkSource().createPageRenderLinkWithContext(Show.class, false, getClassDescriptor().getType(), getBean());
	}
}
