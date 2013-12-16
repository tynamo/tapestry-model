package org.tynamo.model.jpa.components;

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
import org.tynamo.services.DescriptorService;

import java.util.HashSet;
import java.util.Set;

public class ElasticModelSearch extends GenericModelSearch {

	@Inject
	private Node node;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	TypeCoercer typeCoercer;


	@SuppressWarnings("unchecked")
	@Override
	protected GridDataSource createGridDataSource() {

		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(getBeanType());

		if (classDescriptor.supportsExtension(ElasticSearchExtension.class) && getSearchTerms() != null) {
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

			for (SearchHit h : searchResponse.getHits())
				resultIds.add(typeCoercer.coerce(h.getId(), idType));

			return getGridDataSourceProvider().createGridDataSource(getBeanType(), resultIds, getActiveFilterMap());
		}
		return super.createGridDataSource();
	}

	@Override
	public boolean isUsedAsSearchFilter(TynamoClassDescriptor classDescriptor, TynamoPropertyDescriptor propertyDescriptor) {
		if (classDescriptor.supportsExtension(ElasticSearchExtension.class) && propertyDescriptor.supportsExtension(ElasticSearchFieldDescriptor.class))
			return false;
		else
			return super.isUsedAsSearchFilter(classDescriptor, propertyDescriptor);
	}
}