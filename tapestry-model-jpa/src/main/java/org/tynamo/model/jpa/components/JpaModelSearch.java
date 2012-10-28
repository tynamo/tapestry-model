package org.tynamo.model.jpa.components;

import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.jpa.JpaGridDataSource;
import org.tynamo.base.GenericModelSearch;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.model.jpa.SearchableJpaGridDataSource;
import org.tynamo.search.SearchFilterPredicate;

public class JpaModelSearch extends GenericModelSearch {
	@Inject
	private EntityManager entityManager;

	@Override
	public GridDataSource createGridDataSource() {
		// return new TynamoGridDataSource(persistenceService, beanType);
		// return new HibernateGridDataSource(session, beanType);
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap = getActiveFilterMap();

		java.util.List<TynamoPropertyDescriptor> searchablePropertyDescriptors = getSearchablePropertyDescriptors();
		// NOTE does it make sense to return a different grid implementation in this case, or perhaps we should always use the same one
		// regardless?
		if (propertySearchFilterMap.size() <= 0)
			if (searchablePropertyDescriptors.size() <= 0 || getSearchTerms() == null || getSearchTerms().length() <= 0)
				return new JpaGridDataSource(entityManager, getBeanType());

		return new SearchableJpaGridDataSource(entityManager, getBeanType(), propertySearchFilterMap,
			getSearchablePropertyDescriptors(), getSearchTerms());
	}
}
