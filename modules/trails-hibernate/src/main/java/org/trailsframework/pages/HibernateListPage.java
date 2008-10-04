package org.trails.page;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.persistence.HibernatePersistenceService;

public abstract class HibernateListPage extends ListPage
{

	@InjectObject("service:trails.hibernate.PersistenceService")
	public abstract HibernatePersistenceService getHibernatePersistenceService();

	/**
	 * @todo: remove when the components reuse issue goes away.
	 */
	public HibernatePersistenceService getPersistenceService()
	{
		return getHibernatePersistenceService();
	}

	public void activateTrailsPage(Object[] args, IRequestCycle cycle)
	{
		super.activateTrailsPage(args, cycle);
		setCriteria(DetachedCriteria.forClass(getClassDescriptor().getType()));
	}

	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria Criteria);

	public Class getType()
	{
		return getClassDescriptor().getType();
	}
}
