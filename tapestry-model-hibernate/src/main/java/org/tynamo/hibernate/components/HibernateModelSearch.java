package org.tynamo.hibernate.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.Request;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.tynamo.components.SearchFilters;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.hibernate.SearchableHibernateGridDataSource;
import org.tynamo.search.SearchFilterOperator;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.DisplayNameUtils;

public class HibernateModelSearch {
	public static class Prepare implements ComponentAction<HibernateModelSearch> {
		private static final long serialVersionUID = 1L;

		public void execute(HibernateModelSearch component) {
			// component.doPrepare();
		}
		@Override
		public String toString() {
			return "HibernateModelSearch.Prepare";
		}
	}

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private PersistenceService persistenceService;

	@Inject
	private Messages messages;

	@Parameter(required = true, allowNull = false, autoconnect = true)
	@Property(write = false)
	private Class beanType;

	@Persist
	private String searchTerms;

	@Persist
	private Map<Class, SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate>> filterStateByBeanType;

	@Property(write = false)
	private SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate> displayableDescriptorMap;

	@Property
	private Object bean;

	@InjectComponent
	private SearchFilters searchFilters;

	@Inject
	private FullTextSession session;

	@Environmental
	private FormSupport formSupport;

	// void setupRender() {
	// formSupport.storeAndExecute(this, new Prepare());
	// }

	// void doPrepare() {
	void onPrepareFromSearchFilterForm() {

		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanType);

		if (filterStateByBeanType == null) filterStateByBeanType = Collections
			.synchronizedMap(new HashMap<Class, SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate>>());
		else displayableDescriptorMap = filterStateByBeanType.get(beanType);

		if (displayableDescriptorMap == null) {
			SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate> map = new TreeMap<TynamoPropertyDescriptor, SearchFilterPredicate>(
				new Comparator<TynamoPropertyDescriptor>() {
					public int compare(TynamoPropertyDescriptor o1, TynamoPropertyDescriptor o2) {
						return DisplayNameUtils.getDisplayName(o1, messages).compareTo(
							DisplayNameUtils.getDisplayName(o2, messages));
					}
				});

			for (TynamoPropertyDescriptor descriptor : classDescriptor.getPropertyDescriptors())
				// FIXME remove all strings for now, decide how to deal with them later
				// TODO perhaps we should create type-specfic default for SearchFilterPredicates?
				// create a new method createSearchFilterPredicate(descriptor)
				if (!descriptor.isNonVisual() && !descriptor.isIdentifier() && !descriptor.isString())
					map.put(descriptor, createSearchFilterPredicate(descriptor.getPropertyType()));
			filterStateByBeanType.put(beanType, map);
			displayableDescriptorMap = map;
		}
	}

	protected SearchFilterPredicate createSearchFilterPredicate(Class propertyType) {
		SearchFilterPredicate predicate = new SearchFilterPredicate();
		predicate.setOperator(SearchFilterOperator.eq);
		if (boolean.class.isAssignableFrom(propertyType)) {
			predicate.setLowValue(Boolean.FALSE);
		} else if (Boolean.class.isAssignableFrom(propertyType)) {
			predicate.setLowValue(Boolean.FALSE);
		} else if (String.class.isAssignableFrom(propertyType)) {
			predicate.setOperator(SearchFilterOperator.contains);
		}

		return predicate;
	}

	public boolean isSearchable() throws ParseException {
		boolean searchable = descriptorService.getClassDescriptor(beanType).isSearchable();
		if (!searchable) return false;
		// hide the search field if there are no results
		return !isSearchCriteriaSet() && getSource().getAvailableRows() <= 0 ? false : true;
	}

	public boolean isFiltersAvailable() {
		return displayableDescriptorMap != null && displayableDescriptorMap.size() > 0;
	}

	public boolean isSearchCriteriaSet() {
		return getSearchTerms() != null || getActiveFilterMap().size() > 0;
	}

	public void resetSearchCriteria() {
		setSearchTerms(null);
		filterStateByBeanType.put(beanType, null);
	}

	@Inject
	private Request request;

	public Map<TynamoPropertyDescriptor, SearchFilterPredicate> getActiveFilterMap() {
		// may be null if session has expired
		if (filterStateByBeanType == null) return Collections.emptyMap();
		SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate> descriptorMap = filterStateByBeanType.get(beanType);
		if (descriptorMap == null) return Collections.emptyMap();
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> activeDescriptorMap = new HashMap<TynamoPropertyDescriptor, SearchFilterPredicate>();
		for (Entry<TynamoPropertyDescriptor, SearchFilterPredicate> entry : descriptorMap.entrySet())
			if (entry.getValue().isEnabled()) activeDescriptorMap.put(entry.getKey(), entry.getValue());
		return activeDescriptorMap;
	}

	/**
	 * The source of data for the Grid to display. This will usually be a List or array but can also be an explicit GridDataSource
	 * 
	 * @throws ParseException
	 */
	public GridDataSource getSource() throws ParseException {
		// return new TynamoGridDataSource(persistenceService, beanType);
		// return new HibernateGridDataSource(session, beanType);
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap = getActiveFilterMap();

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
