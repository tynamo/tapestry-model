package org.tynamo.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.components.SearchFilters;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.EmbeddedDescriptor;
import org.tynamo.descriptor.ObjectReferenceDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.extension.BeanModelExtension;
import org.tynamo.search.SearchFilterOperator;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.SearchableGridDataSourceProvider;
import org.tynamo.util.DisplayNameUtils;

public abstract class GenericModelSearch {

	@Inject
	private DescriptorService descriptorService;

	@Inject
	SearchableGridDataSourceProvider gridDataSourceProvider;

	@Inject
	private Messages messages;

	@InjectComponent
	private SearchFilters searchFilters;

	@Parameter(required = true, allowNull = false)
	private Class beanType;

	@Persist
	private String searchTerms;

	@Persist
	private Map<Class, SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate>> filterStateByBeanType;

	@Property(write = false)
	private SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate> displayableFilterDescriptorMap;

	private List<TynamoPropertyDescriptor> searchablePropertyDescriptors;

	private GridDataSource gridDataSource;

	public Class getBeanType() {
		return beanType;
	}

	public SearchableGridDataSourceProvider getGridDataSourceProvider() {
		return gridDataSourceProvider;
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

	protected void doPrepare() {
		if (displayableFilterDescriptorMap != null && searchablePropertyDescriptors != null) return;

		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanType);

		if (filterStateByBeanType == null) filterStateByBeanType = Collections
			.synchronizedMap(new HashMap<Class, SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate>>());
		else displayableFilterDescriptorMap = filterStateByBeanType.get(beanType);

		if (displayableFilterDescriptorMap == null || searchablePropertyDescriptors == null) {
			SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate> map =
					new TreeMap<TynamoPropertyDescriptor, SearchFilterPredicate>(new TynamoPropertyDescriptorComparator());

			List<TynamoPropertyDescriptor> descriptors = new ArrayList<TynamoPropertyDescriptor>();

			for (TynamoPropertyDescriptor descriptor : classDescriptor.getPropertyDescriptors()) {
				// FIXME remove all strings for now, decide how to deal with them later
				// TODO perhaps we should create type-specfic default for SearchFilterPredicates?
				// create a new method createSearchFilterPredicate(descriptor)
				if (descriptor.isNonVisual() || descriptor.isIdentifier() || !descriptor.isSearchable()) continue;
				if (isUsedAsSearchFilter(classDescriptor, descriptor)) {
					// at least for now, don't allow creating filters for complex properties
					if (descriptor instanceof ObjectReferenceDescriptor || descriptor instanceof CollectionDescriptor
						|| descriptor instanceof EmbeddedDescriptor || descriptor.supportsExtension(BeanModelExtension.class))
						continue;
					map.put(descriptor, createSearchFilterPredicate(descriptor.getPropertyType()));
				}
				else descriptors.add(descriptor);
			}

			if (displayableFilterDescriptorMap == null) {
				filterStateByBeanType.put(beanType, map);
				displayableFilterDescriptorMap = map;
			}

				searchablePropertyDescriptors = descriptors;
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

	public boolean isSearchable() {
		boolean searchable = descriptorService.getClassDescriptor(beanType).isSearchable();
		if (!searchable) return false;
		// hide the search field if there are no results
		return isSearchCriteriaSet() || getGridDataSource().getAvailableRows() > 0;
	}

	public boolean isSearchFiltersAvailable() {
		return displayableFilterDescriptorMap != null && displayableFilterDescriptorMap.size() > 0;
	}

	public boolean isSearchCriteriaSet() {
		return getSearchTerms() != null || getActiveFilterMap().size() > 0;
	}

	public void resetSearchCriteria() {
		setSearchTerms(null);
		filterStateByBeanType.put(beanType, null);
	}

	public boolean isUsedAsSearchFilter(TynamoClassDescriptor classDescriptor, TynamoPropertyDescriptor propertyDescriptor) {
		// the default implementation simply treats all non-strings as filters
		return !propertyDescriptor.isString();
	}

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

	public final GridDataSource getGridDataSource() {
		// we *must* initialize the component before returning the dataSource. Since getGridDataSource() is public,
		// it's possible it'll be called before setupRender.
		doPrepare();
		if (gridDataSource == null) gridDataSource = createGridDataSource();
		return gridDataSource;
	}

	protected GridDataSource createGridDataSource() {
		if (searchTerms != null) {
			return gridDataSourceProvider.createGridDataSource(beanType, getActiveFilterMap(), searchablePropertyDescriptors, searchTerms);
		} else {
			return gridDataSourceProvider.createGridDataSource(beanType, null, getActiveFilterMap());
		}
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	private class TynamoPropertyDescriptorComparator implements Comparator<TynamoPropertyDescriptor>
	{
		public int compare(TynamoPropertyDescriptor o1, TynamoPropertyDescriptor o2) {
			return DisplayNameUtils.getDisplayName(o1, messages).compareTo(
					DisplayNameUtils.getDisplayName(o2, messages));
		}
	}
}