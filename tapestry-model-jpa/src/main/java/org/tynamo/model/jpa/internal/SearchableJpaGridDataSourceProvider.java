package org.tynamo.model.jpa.internal;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.jpa.JpaGridDataSource;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.model.jpa.SearchableJpaGridDataSource;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.SearchableGridDataSourceProvider;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public GridDataSource createGridDataSource(Class entityType,
	                                           Set includedIds,
	                                           Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap) {
		return new SearchableJpaGridDataSource(entityManager, entityType, includedIds, propertySearchFilterMap);
	}

	@Override
	public GridDataSource createGridDataSource(Class entityType,
	                                           Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap,
	                                           List<TynamoPropertyDescriptor> searchablePropertyDescriptors,
	                                           String searchString) {
		return new SearchableJpaGridDataSource(entityManager, entityType, propertySearchFilterMap, searchablePropertyDescriptors, searchString);
	}

}
