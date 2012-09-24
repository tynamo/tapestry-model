package org.tynamo.hibernate.components;

import java.util.ArrayList;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.tynamo.components.SearchFilters;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.hibernate.SearchableHibernateGridDataSource;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;

public class HibernateModelSearch {
	@Inject
	private DescriptorService descriptorService;

	@Inject
	private PersistenceService persistenceService;

	@Parameter(required = true, allowNull = false, autoconnect = true)
	@Property(write = false)
	private Class beanType;

	@Persist
	private String searchTerms;

	@Property
	private Object bean;

	@InjectComponent
	private SearchFilters searchFilters;

	@Inject
	private FullTextSession session;

	public boolean isSearchable() throws ParseException {
		boolean searchable = descriptorService.getClassDescriptor(beanType).isSearchable();
		if (!searchable) return false;
		// hide the search field if there are no results
		return !isSearchCriteriaSet() && getSource().getAvailableRows() <= 0 ? false : true;
	}

	public boolean isFiltersAvailable() {
		return searchFilters.getDisplayableDescriptorMap() != null
			&& searchFilters.getDisplayableDescriptorMap().size() > 0;
	}

	public boolean isSearchCriteriaSet() {
		return getSearchTerms() != null || searchFilters.getActiveFilterMap().size() > 0;
	}

	public void resetSearchCriteria() {
		setSearchTerms(null);
		searchFilters.resetFilters();
	}

	@Inject
	private Request request;

	/**
	 * The source of data for the Grid to display. This will usually be a List or array but can also be an explicit GridDataSource
	 * 
	 * @throws ParseException
	 */
	public GridDataSource getSource() throws ParseException {
		// return new TynamoGridDataSource(persistenceService, beanType);
		// return new HibernateGridDataSource(session, beanType);
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap = searchFilters.getActiveFilterMap();

		if (getSearchTerms() == null) return new SearchableHibernateGridDataSource(session, beanType, propertySearchFilterMap);

		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanType);
		java.util.List<String> fieldNames = new ArrayList<String>();
		java.util.List<TynamoPropertyDescriptor> propertyDescriptors = classDescriptor.getPropertyDescriptors();
		for (TynamoPropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyDescriptor.isSearchable() && propertyDescriptor.isString())
				fieldNames.add(propertyDescriptor.getName());
		}
		// don't bother with a text query if there are no @Fields
		if (fieldNames.size() <= 0)
			return new SearchableHibernateGridDataSource(session, beanType, propertySearchFilterMap);

		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_36, fieldNames.toArray(new String[0]),
			new StandardAnalyzer(Version.LUCENE_36));
		// parser.setDefaultOperator(QueryParser.AND_OPERATOR); // overrides the default OR_OPERATOR, so that all words in the search are
		// required
		org.apache.lucene.search.Query query = parser.parse(getSearchTerms());

		QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity(beanType).get();

		// NOTE Hibernate Search DSL checks that the fields exists, otherwise it throws exceptions. Lucene is more forgiving
		// QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity( beanType ).get();
		// org.apache.lucene.search.Query query = qb.keyword().onFields(fieldNames.toArray(new String[0])).matching(searchTerms).createQuery();
		return new SearchableHibernateGridDataSource(session, beanType, session.createFullTextQuery(query, beanType),
			propertySearchFilterMap);
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

}
