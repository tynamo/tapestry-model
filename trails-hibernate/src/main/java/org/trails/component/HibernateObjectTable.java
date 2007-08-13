package org.trails.component;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.persistence.HibernatePersistenceService;

@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class HibernateObjectTable extends ObjectTable
{

	@Asset(value = "/org/trails/component/ObjectTable.html")
	public abstract IAsset get$template();

	@InjectObject("spring:persistenceService")
	public abstract HibernatePersistenceService getHibernatePersistenceService();

	/**
	 * @return
	 * @todo: remove when the components reuse issue goes away.
	 */
	public HibernatePersistenceService getPersistenceService()
	{
		return getHibernatePersistenceService();
	}


	@Parameter
	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria criteria);

	public Object getSource()
	{
		if (getInstances() == null)
		{
			return new HibernateTableModel(getClassDescriptor().getType(), getHibernatePersistenceService(), getCriteria());
		}
		return getInstances();
	}

	@Parameter(cache = true, required = false)
	public abstract int getIndex();

	public abstract void setIndex(int index);

	@Parameter(cache = false)
	public abstract ITableColumn getColumn();

	public abstract void setColumn(ITableColumn column);
}
