package org.tynamo.hibernate.components;

import org.apache.tapestry5.grid.GridDataSource;
import org.tynamo.base.GenericModelSearch;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterPredicate;

import java.util.Map;

public class HibernateModelSearch extends GenericModelSearch {

	protected GridDataSource createGridDataSource() {
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap = getActiveFilterMap();
		return getGridDataSourceProvider().createGridDataSource(getBeanType(), null, propertySearchFilterMap);
	}

	protected GridDataSource createGridDataSource(org.apache.lucene.search.Query query) {
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap = getActiveFilterMap();
		return getGridDataSourceProvider().createGridDataSource(getBeanType(), propertySearchFilterMap, getSearchablePropertyDescriptors(), getSearchTerms());
	}

}
