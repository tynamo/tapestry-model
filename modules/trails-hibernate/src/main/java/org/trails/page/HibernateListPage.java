package org.trails.page;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.callback.HibernateListCallback;
import org.trails.persistence.HibernatePersistenceService;


public abstract class HibernateListPage extends ListPage
{

	public void activateExternalPage(Object[] args, IRequestCycle cycle)
	{
		super.activateExternalPage(args, cycle);
		setCriteria(DetachedCriteria.forClass(getType()));
		getCallbackStack().getStack().clear();
	}

	@Persist
	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria Criteria);

	public void pushCallback()
	{
		getCallbackStack().push(new HibernateListCallback(getPageName(), getType(), getCriteria()));
	}

	public void reloadInstances()
	{
		// do nothing;
	}
	
	public HibernatePersistenceService getHibernatePersistenceService() {
		return (HibernatePersistenceService)getPersistenceService();
	}
}
