package org.trails.component;

import java.util.Iterator;

import org.apache.commons.lang.SerializationUtils;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.trails.persistence.PersistenceService;

public class TrailsTableModel implements IBasicTableModel
{
    private PersistenceService persistenceService;
    
    private DetachedCriteria criteria;
    
    public TrailsTableModel(PersistenceService persistenceService, DetachedCriteria criteria)
    {
        super();
        this.persistenceService = persistenceService;
        this.criteria = criteria;
    }

    public Iterator getCurrentPageRows(int startIndex, int maxResults, ITableColumn column,
            boolean asc)
    {
        DetachedCriteria clonedCriteria = (DetachedCriteria)SerializationUtils.clone(getCriteria());
        TrailsTableColumn trailsTableColumn = (TrailsTableColumn)column;
        if (trailsTableColumn != null)
        {
            String sortProperty = trailsTableColumn.getPropertyDescriptor().getName();
            clonedCriteria.addOrder(asc ? Order.asc(sortProperty) : Order.desc(sortProperty));
        }
        return getPersistenceService().getInstances(clonedCriteria, startIndex, maxResults).iterator();
    }

    public int getRowCount()
    {
        // doing a count alters the criteria
        DetachedCriteria clonedCriteria = (DetachedCriteria)SerializationUtils.clone(getCriteria());
        return getPersistenceService().count(clonedCriteria);
    }

    public DetachedCriteria getCriteria()
    {
        return criteria;
    }

    public void setCriteria(DetachedCriteria criteria)
    {
        this.criteria = criteria;
    }

    public PersistenceService getPersistenceService()
    {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService)
    {
        this.persistenceService = persistenceService;
    }

}
