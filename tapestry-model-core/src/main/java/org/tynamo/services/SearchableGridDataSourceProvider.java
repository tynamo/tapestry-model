package org.tynamo.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.grid.GridDataSource;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterPredicate;

public interface SearchableGridDataSourceProvider {
	public GridDataSource createGridDataSource(Class entityType);

	public GridDataSource createGridDataSource(Class entityType,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap, Set includedIds);

	public GridDataSource createGridDataSource(Class entityType,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap,
		List<TynamoPropertyDescriptor> searchablePropertyDescriptors, String... searchTerms);

}
