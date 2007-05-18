package org.trails.page;

import org.apache.tapestry.IRequestCycle;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.callback.HibernateListCallback;


public abstract class HibernateListPage extends ListPage
{

	public void activateExternalPage(Object[] args, IRequestCycle cycle)
	{
		super.activateExternalPage(args, cycle);
		setCriteria(DetachedCriteria.forClass(getType()));
		getCallbackStack().getStack().clear();
	}

	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria Criteria);

	public void pushCallback()
	{
		getCallbackStack().push(new HibernateListCallback(getPageName(), getTypeName(), getType(), getCriteria()));
	}

	public void reloadInstances()
	{
		// no nothing;
	}
}
