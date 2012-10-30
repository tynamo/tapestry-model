package org.tynamo.model.jpa.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.tynamo.model.elasticsearch.annotations.ElasticSearchField;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;
import org.tynamo.model.elasticsearch.mapping.ModelMapper;
import org.tynamo.model.elasticsearch.util.ReflectionUtil;
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

		if (getSearchTerms() == null) return getGridDataSourceProvider().createGridDataSource(getBeanType());

		// don't bother with a text query if there are no @ElasticSearchFields
		if (getSearchablePropertyDescriptors().size() <= 0)
			return getGridDataSourceProvider().createGridDataSource(getBeanType());

		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(getBeanType());

		Client client = node.client();

		// We shouldn't invoke this GridDataSource unless MappingUtil.isSearchable(descriptor.getBeanType() is true
		ModelMapper mapper = mapperFactory.getMapper(getBeanType());

		// FIXME consider whether we should .setTypes("entityType")
		SearchResponse searchResponse = client.prepareSearch(mapper.getIndexName())
			.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.queryString(getSearchTerms()))).execute().actionGet();

		if (searchResponse.getHits().getTotalHits() <= 0) return new EmptyGridDataSource(getBeanType());

		@SuppressWarnings("rawtypes")
		List resultIds = new ArrayList();
		@SuppressWarnings("rawtypes")
		Class idType = classDescriptor.getIdentifierDescriptor().getPropertyType();

		for (SearchHit h : searchResponse.hits())
			resultIds.add(typeCoercer.coerce(h.getId(), idType));

		return getGridDataSourceProvider().createGridDataSource(getBeanType(), propertySearchFilterMap, resultIds);
	}

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private Node node;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	TypeCoercer typeCoercer;

	private List<String> elasticSearchFieldNames;

	@Override
	protected void doPrepare() {
		// collect the field names with @ElasticSearchField so we don't have to re-fetch them all time
		// TODO does it accept class arguments?
		elasticSearchFieldNames = ReflectionUtil.getFieldNamesWithAnnotation(getBeanType(), ElasticSearchField.class);
		super.doPrepare();
	}

	@Override
	public boolean isDisplayableFilter(TynamoPropertyDescriptor propertyDescriptor) {
		return !elasticSearchFieldNames.contains(propertyDescriptor.getName());
	}
}