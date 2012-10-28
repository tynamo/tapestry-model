package org.tynamo.base;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.internal.services.EmptyGridDataSource;
import org.tynamo.search.SearchFilterPredicate;

public class SolrModelSearch extends GenericModelSearch {
	/**
	 * The source of data for the Grid to display. This will usually be a List or array but can also be an explicit GridDataSource
	 *
	 * @throws ParseException
	 */
	public GridDataSource createGridDataSource() {
		// return new TynamoGridDataSource(persistenceService, beanType);
		// return new HibernateGridDataSource(session, beanType);
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap = getActiveFilterMap();

		if (getSearchTerms() == null) return getGridDataSourceProvider().createGridDataSource(getBeanType());

		// don't bother with a text query if there are no @Fields
		if (getSearchablePropertyDescriptors().size() <= 0)
			return getGridDataSourceProvider().createGridDataSource(getBeanType());

		final SolrParams params = new SolrQuery(getSearchTerms());
		QueryResponse response;
		try {
			response = solrServer.query(params);
		} catch (SolrServerException e) {
			throw new RuntimeException(String.format("Couldn't invoke solrQuery with the following search terms: '%s'",
				getSearchTerms()), e);
		}
		// assertEquals(1L, response.getResults().getNumFound());
		// assertEquals(1L, response.getResults().get(0).get("id"));

		if (response.getResults().size() <= 0) return new EmptyGridDataSource(getBeanType());

		Set ids = new HashSet(response.getResults().size());
		for (SolrDocument document : response.getResults()) {
			// is there a sure way of obtaining the id? do not we need to transform to a bean first?
			ids.add(document.get("id"));
		}

		return getGridDataSourceProvider().createGridDataSource(getBeanType(), propertySearchFilterMap, ids);
	}

	@Inject
	private SolrServer solrServer;

	@Inject
	private DocumentObjectBinder documentObjectBinder;

	private SolrInputDocument document;

	@Override
	protected void doPrepare() {
		// assigne the document object as a property here so we don't have to continuously re-fetch it later
		// TODO does it accept class arguments?
		document = documentObjectBinder.toSolrInputDocument(getBeanType());
		super.doPrepare();
	}

	@Override
	public boolean isDisplayableFilter(TynamoPropertyDescriptor propertyDescriptor) {
		return !document.getFieldNames().contains(propertyDescriptor.getName());
	}
}