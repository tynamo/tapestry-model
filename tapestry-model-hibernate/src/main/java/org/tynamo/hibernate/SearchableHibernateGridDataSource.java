package org.tynamo.hibernate;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.tynamo.descriptor.TynamoPropertyDescriptor;

public class SearchableHibernateGridDataSource implements GridDataSource {

	private FullTextQuery fullTextQuery;
	private Session session;
	private Object[] filteringPropertyValues;
	private Class entityType;
  private int startIndex;
  private List preparedResults;
	

	public SearchableHibernateGridDataSource(Session session, Class entityType, FullTextQuery fullTextQuery, Object... filteringPropertyValues) {
		this.session= session;
		this.entityType = entityType;
		this.fullTextQuery = fullTextQuery;
		// every other is TynamoPropertyDescriptor, every other a value
		this.filteringPropertyValues = filteringPropertyValues;
		if (filteringPropertyValues != null && filteringPropertyValues.length %2 != 0) throw new IllegalArgumentException("filteringPropertyValues is expected to contain a TynamoPropertyDescriptor+value pairs. The length of the array was not an even number");
	}
  
  /**
   * Returns the total number of rows for the configured entity type.
   */
  public int getAvailableRows()
  {
		Criteria criteria = session.createCriteria(entityType);
		applyAdditionalConstraints(criteria);

		if (fullTextQuery == null) {
			criteria.setProjection(Projections.rowCount());
			Number result = (Number) criteria.uniqueResult();
			return result.intValue();
		}

		fullTextQuery.setCriteriaQuery(criteria);
		return fullTextQuery.getResultSize();
  }
  
  /**
   * Prepares the results, performing a query (applying the sort results, and the provided start and end index). The
   * results can later be obtained from {@link #getRowValue(int)} }.
   *
   * @param startIndex      index, from zero, of the first item to be retrieved
   * @param endIndex        index, from zero, of the last item to be retrieved
   * @param sortConstraints zero or more constraints used to set the order of the returned values
   */
  public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints)
  {
      assert sortConstraints != null;
      Criteria crit = session.createCriteria(entityType);

      crit.setFirstResult(startIndex).setMaxResults(endIndex - startIndex + 1);

      for (SortConstraint constraint : sortConstraints)
      {

          String propertyName = constraint.getPropertyModel().getPropertyName();

          switch (constraint.getColumnSort())
          {

              case ASCENDING:

                  crit.addOrder(Order.asc(propertyName));
                  break;

              case DESCENDING:
                  crit.addOrder(Order.desc(propertyName));
                  break;

              default:
          }
      }

      applyAdditionalConstraints(crit);

      this.startIndex = startIndex;
      
      if (fullTextQuery == null) {
      	preparedResults = crit.list();
      	return;
      }
      
      fullTextQuery.setCriteriaQuery(crit);
      preparedResults = fullTextQuery.list();
  }

  /**
   * Invoked after the main criteria has been set up (firstResult, maxResults and any sort contraints). This gives
   * subclasses a chance to apply additional constraints before the list of results is obtained from the criteria.
   * This implementation does nothing and may be overridden.
   */
  protected void applyAdditionalConstraints(Criteria crit)
  {
  	if (filteringPropertyValues == null || filteringPropertyValues.length == 0) return;
  	for (int i = 0; i < filteringPropertyValues.length; i+=2) {
  		// add the filtering properties to the criteria
  		if (!(filteringPropertyValues[i] instanceof TynamoPropertyDescriptor)) throw new IllegalArgumentException("filteringPropertyValues at index " + i + " is of type " + filteringPropertyValues[i].getClass() + " but " + TynamoPropertyDescriptor.class.getSimpleName() + " was expected");
  		TynamoPropertyDescriptor propertyDescriptor = (TynamoPropertyDescriptor)filteringPropertyValues[i];
  		// TODO we should log a warning
  		if (propertyDescriptor.isObjectReference() || !propertyDescriptor.isSearchable()) continue;
  		crit.add(Restrictions.eq(propertyDescriptor.getName(), filteringPropertyValues[i+1]));
  	}
  	
  } 
  
  /**
   * Returns a row value at the given index (which must be within the range defined by the call to {@link
   * #prepare(int, int, java.util.List)} ).
   *
   * @param index of object
   * @return object at that index
   */
  public Object getRowValue(int index)
  {
      return preparedResults.get(index - startIndex);
  }

  /**
   * Returns the entity type, as provided via the constructor.
   */
  public Class getRowType()
  {
      return entityType;
  }

}
