package org.tynamo.base;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.tynamo.components.SearchFilters;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterOperator;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.DescriptorService;
import org.tynamo.util.DisplayNameUtils;

import java.util.*;
import java.util.Map.Entry;

public abstract class GenericModelSearch {

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private Messages messages;

	@Parameter(required = true, allowNull = false, autoconnect = true)
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

	public Class getBeanType() {
		return beanType;
	}

	protected DescriptorService getDescriptorService() {
		return descriptorService;
	}

	void setupRender() {
		doPrepare();
	}

	// use onPrepareForSubmit + setupRender rather than onPrepare to initialize displayableDescriptorMap in time to decide
	// whether to hide the whole form
	// void onPrepareSearchFilterForm() {
	void onPrepareForSubmitFromSearchFilterForm() {
		doPrepare();
	}

	void doPrepare() {
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
				if (!descriptor.isTransient() && !descriptor.isNonVisual() && !descriptor.isIdentifier()
					&& !descriptor.isString())
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

	public boolean isSearchable()
	{
		boolean searchable = descriptorService.getClassDescriptor(beanType).isSearchable();
		if (!searchable) return false;
		// hide the search field if there are no results
		return isSearchCriteriaSet() || getGridDataSource().getAvailableRows() > 0;
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

	public abstract GridDataSource getGridDataSource();

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}
}