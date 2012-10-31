package org.tynamo.model.jpa.internal;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.jpa.JpaGridDataSource;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.model.jpa.SearchableJpaGridDataSource;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.SearchableGridDataSourceProvider;

public class SearchableJpaGridDataSourceProvider implements SearchableGridDataSourceProvider {
	private EntityManager entityManager;

	public SearchableJpaGridDataSourceProvider(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public GridDataSource createGridDataSource(Class entityType) {
		return new JpaGridDataSource(entityManager, entityType);
	}

	@Override
	public GridDataSource createGridDataSource(Class entityType, Set includedIds,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap) {
		return new SearchableJpaGridDataSource(entityManager, entityType, includedIds, propertySearchFilterMap);
	}

	@Override
	public GridDataSource createGridDataSource(Class entityType,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap,
		List<TynamoPropertyDescriptor> searchablePropertyDescriptors, String... searchTerms) {
		return new SearchableJpaGridDataSource(entityManager, entityType, propertySearchFilterMap,
			searchablePropertyDescriptors, searchTerms);
	}

}
