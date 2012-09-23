package org.tynamo.components;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.FormSupport;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterOperator;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.DescriptorService;
import org.tynamo.util.DisplayNameUtils;

public class SearchFilters
{
	public static class Prepare implements ComponentAction<SearchFilters> {
		private static final long serialVersionUID = 1L;

		public void execute(SearchFilters component) {
			component.doPrepare();
		}

		@Override
		public String toString() {
			return "SearchFilters.Prepare";
		}
	}

	@Inject
	private Messages messages;

	@Environmental
	private FormSupport formSupport;

	@Inject
	private DescriptorService descriptorService;

  @Parameter(required = true, allowNull = false, autoconnect = true)
	private Class beanType;

	@Persist
	private Map<Class, SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate>> filterStateByBeanType;

	@Property
	private Entry<TynamoPropertyDescriptor, Object> entry;

	private SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate> displayableDescriptorMap;

	public SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate> getDisplayableDescriptorMap() {
		return displayableDescriptorMap;
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

	public void resetFilters() {
		filterStateByBeanType.put(beanType, null);
	}

	void setupRender() {
		formSupport.storeAndExecute(this, new Prepare());
	}

	void doPrepare() {
		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanType);

		if (filterStateByBeanType == null)
			filterStateByBeanType = Collections
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

}
