package org.trails.component;

import org.apache.commons.lang.SerializationUtils;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.trails.persistence.HibernatePersistenceService;

import java.util.Iterator;

public class HibernateTableModel implements IBasicTableModel
{

	private HibernatePersistenceService persistenceService;

	private Class entityType;
	private DetachedCriteria criteria;

	public HibernateTableModel(Class entityType, HibernatePersistenceService persistenceService, DetachedCriteria criteria)
	{
		super();
		this.entityType = entityType;
		this.persistenceService = persistenceService;
		this.criteria = criteria;
	}

	public void setPersistenceService(HibernatePersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public void setCriteria(DetachedCriteria criteria)
	{
		this.criteria = criteria;
	}

	public void setEntityType(Class entityType)
	{
		this.entityType = entityType;
	}

	public Iterator getCurrentPageRows(int startIndex, int maxResults, ITableColumn column, boolean asc)
	{
		DetachedCriteria clonedCriteria = (DetachedCriteria) SerializationUtils.clone(criteria);
		if (column != null && column instanceof TrailsTableColumn)
		{
			TrailsTableColumn trailsTableColumn = (TrailsTableColumn) column;
			String sortProperty = trailsTableColumn.getPropertyDescriptor().getName();
			clonedCriteria.addOrder(asc ? Order.asc(sortProperty) : Order.desc(sortProperty));
		}
		return persistenceService.getInstances(entityType, clonedCriteria, startIndex, maxResults).iterator();
	}

	public int getRowCount()
	{
		// doing a count alters the criteria
		DetachedCriteria clonedCriteria = (DetachedCriteria) SerializationUtils.clone(criteria);
		return persistenceService.count(entityType, clonedCriteria);
	}
}
