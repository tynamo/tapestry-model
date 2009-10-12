package org.tynamo.examples.recipe.pages;


import org.apache.tapestry5.Link;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.tynamo.hibernate.pages.HibernateModelPage;
import org.tynamo.util.DisplayNameUtils;

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

	public String getEditLinkMessage()
	{
		return getMessages().format("org.tynamo.i18n.edit", DisplayNameUtils.getDisplayName(getClassDescriptor(), getMessages()));
	}

	public String getTitle()
	{
		return getMessages().format("org.tynamo.i18n.show", getBean().toString(), getMessages());
	}

	public Link onActionFromDelete()
	{
		getPersitenceService().remove(getBean());
		return back();
	}

	@Override
	public BeanModel createBeanModel(Class clazz)
	{
		return getBeanModelSource().createDisplayModel(clazz, getMessages());
	}
}
