package org.tynamo.model.jpa.components;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.tynamo.base.GenericModelSearch;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.internal.services.EmptyGridDataSource;
import org.tynamo.model.elasticsearch.descriptor.ElasticSearchExtension;
import org.tynamo.model.elasticsearch.descriptor.ElasticSearchFieldDescriptor;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.DescriptorService;

public class ElasticModelSearch extends GenericModelSearch {
	/**
	 * The source of data for the Grid to display. This will usually be a List or array but can also be an explicit GridDataSource
	 *
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public GridDataSource createGridDataSource() {
		// return new TynamoGridDataSource(persistenceService, beanType);
		// return new HibernateGridDataSource(session, beanType);
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap = getActiveFilterMap();

		if (getSearchTerms() == null && propertySearchFilterMap.size() <= 0)
			return getGridDataSourceProvider().createGridDataSource(getBeanType());

		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(getBeanType());

		// just invoke a plain database search if the descriptor doesn't support ElastiSearch
		if (!classDescriptor.supportsExtension(ElasticSearchExtension.class))
			return getGridDataSourceProvider().createGridDataSource(getBeanType(), propertySearchFilterMap,
				getSearchablePropertyDescriptors(), getSearchTerms());

		Client client = node.client();

		// We shouldn't invoke this GridDataSource unless MappingUtil.isSearchable(descriptor.getBeanType() is true
		ElasticSearchExtension elasticDescriptor = classDescriptor.getExtension(ElasticSearchExtension.class);

		// FIXME consider whether we should .setTypes("entityType")
		SearchResponse searchResponse = client.prepareSearch(elasticDescriptor.getIndexName())
			.setTypes(elasticDescriptor.getTypeName())
			.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.queryString(getSearchTerms()))).execute().actionGet();

		if (searchResponse.getHits().getTotalHits() <= 0) return new EmptyGridDataSource(getBeanType());

		@SuppressWarnings("rawtypes")
		Set resultIds = new HashSet();
		@SuppressWarnings("rawtypes")
		Class idType = classDescriptor.getIdentifierDescriptor().getPropertyType();

		for (SearchHit h : searchResponse.hits())
			resultIds.add(typeCoercer.coerce(h.getId(), idType));

		return getGridDataSourceProvider().createGridDataSource(getBeanType(), resultIds, propertySearchFilterMap);
	}

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private Node node;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	TypeCoercer typeCoercer;

	@Override
	public boolean isUsedAsSearchFilter(TynamoClassDescriptor classDescriptor, TynamoPropertyDescriptor propertyDescriptor) {
		if (classDescriptor.supportsExtension(ElasticSearchExtension.class))
			return !propertyDescriptor.supportsExtension(ElasticSearchFieldDescriptor.class);
		return super.isUsedAsSearchFilter(classDescriptor, propertyDescriptor);
	}
}