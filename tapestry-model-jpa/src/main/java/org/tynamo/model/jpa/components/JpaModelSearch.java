package org.tynamo.model.jpa.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.jpa.JpaGridDataSource;
import org.tynamo.base.GenericModelSearch;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.model.jpa.SearchableJpaGridDataSource;
import org.tynamo.search.SearchFilterPredicate;

public class JpaModelSearch extends GenericModelSearch {
	@Inject
	private EntityManager entityManager;

	private java.util.List<TynamoPropertyDescriptor> getSearchablePropertyDescriptors() {
		TynamoClassDescriptor classDescriptor = getDescriptorService().getClassDescriptor(getBeanType());
		java.util.List<TynamoPropertyDescriptor> propertyDescriptors = classDescriptor.getPropertyDescriptors();
		if (propertyDescriptors.size() <= 0) return Collections.emptyList();
		java.util.List<TynamoPropertyDescriptor> searchablePropertyDescriptors = new ArrayList<TynamoPropertyDescriptor>();
		for (TynamoPropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyDescriptor.isSearchable() && propertyDescriptor.isString())
				searchablePropertyDescriptors.add(propertyDescriptor);
		}
		return searchablePropertyDescriptors;
	}

	@Override
	public GridDataSource getGridDataSource() {
		// return new TynamoGridDataSource(persistenceService, beanType);
		// return new HibernateGridDataSource(session, beanType);
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap = getActiveFilterMap();

		java.util.List<TynamoPropertyDescriptor> searchablePropertyDescriptors = getSearchablePropertyDescriptors();
		// NOTE does is make sense to return a different grid implementation in this case, or perhaps we should always use the same one
		// regardless?
		if (getSearchTerms() == null || getSearchTerms().length() <= 0)
			if (searchablePropertyDescriptors.size() <= 0) return new JpaGridDataSource(entityManager, getBeanType());

		return new SearchableJpaGridDataSource(entityManager, getBeanType(), propertySearchFilterMap,
			getSearchablePropertyDescriptors(), getSearchTerms());
	}
}
