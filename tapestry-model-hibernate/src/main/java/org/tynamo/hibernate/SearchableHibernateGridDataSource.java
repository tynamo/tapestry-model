package org.tynamo.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterPredicate;

public class SearchableHibernateGridDataSource implements GridDataSource {

	private FullTextQuery fullTextQuery;
	private Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap;
	private Session session;
	private Class entityType;
  private int startIndex;
  private List preparedResults;

	public SearchableHibernateGridDataSource(Session session, Class entityType,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap) {
		this(session, entityType, null, propertySearchFilterMap);
	}

	public SearchableHibernateGridDataSource(Session session, Class entityType, FullTextQuery fullTextQuery,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap) {
		this.session= session;
		this.entityType = entityType;
		this.fullTextQuery = fullTextQuery;
		this.propertySearchFilterMap = propertySearchFilterMap;
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
		// calling getResultSize() causes HSEARCH000105: Cannot safely compute getResultSize() when a Criteria with restriction is used
		// return fullTextQuery.getResultSize();T
		return fullTextQuery.list().size();
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
		if (propertySearchFilterMap == null || propertySearchFilterMap.size() <= 0) return;
		for (Entry<TynamoPropertyDescriptor, SearchFilterPredicate> entry : propertySearchFilterMap.entrySet())
			crit.add(createCriterion(entry.getKey().getName(), entry.getValue()));
	}

	private Criterion createCriterion(String propertyName, SearchFilterPredicate predicate) {
		switch (predicate.getOperator()) {
		case eq:
			return Restrictions.eq(propertyName, predicate.getLowValue());
		case ne:
			return Restrictions.ne(propertyName, predicate.getLowValue());
		case gt:
			return Restrictions.gt(propertyName, predicate.getLowValue());
		case ge:
			return Restrictions.ge(propertyName, predicate.getLowValue());
		case lt:
			return Restrictions.lt(propertyName, predicate.getLowValue());
		case le:
			return Restrictions.le(propertyName, predicate.getLowValue());
		default:
			throw new IllegalArgumentException("Search filtering for operator '" + predicate.getOperator()
				+ "' is not yet supported");
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
