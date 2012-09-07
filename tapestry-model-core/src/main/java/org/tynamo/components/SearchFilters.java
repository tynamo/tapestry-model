package org.tynamo.components;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.util.DisplayNameUtils;

public class SearchFilters
{
	@Inject
	private Messages messages;

	@Inject
	private DescriptorService descriptorService;

  @Parameter(required = true, allowNull = false, autoconnect = true)
	private Class beanType;

	@Persist
	private Map<TynamoClassDescriptor, SortedMap<TynamoPropertyDescriptor, Object>> filterStateByDescriptor;
	
	@Property
	private Entry<TynamoPropertyDescriptor, Object> entry;

	private SortedMap<TynamoPropertyDescriptor, Object> displayableDescriptorMap;

	public SortedMap<TynamoPropertyDescriptor, Object> getDisplayableDescriptorMap() {
		return displayableDescriptorMap;
	}

	public Map<TynamoPropertyDescriptor, Object> getActiveFilterMap() {
		return null;
	}

	void setupRender() {
		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanType);

		if (filterStateByDescriptor == null)
			filterStateByDescriptor = Collections
				.synchronizedMap(new HashMap<TynamoClassDescriptor, SortedMap<TynamoPropertyDescriptor, Object>>());
		else displayableDescriptorMap = filterStateByDescriptor.get(classDescriptor);

		if (displayableDescriptorMap == null) {
			SortedMap<TynamoPropertyDescriptor, Object> map = new TreeMap<TynamoPropertyDescriptor, Object>(
				new Comparator<TynamoPropertyDescriptor>() {
					public int compare(TynamoPropertyDescriptor o1, TynamoPropertyDescriptor o2) {
						return DisplayNameUtils.getDisplayName(o1, messages).compareTo(
							DisplayNameUtils.getDisplayName(o2, messages));
					}
				});

			for (TynamoPropertyDescriptor descriptor : classDescriptor.getPropertyDescriptors())
				// FIXME remove all strings for now, decide how to deal with them later
				if (!descriptor.isNonVisual() && !descriptor.isIdentifier() && !descriptor.isString())
					map.put(descriptor, null);
			filterStateByDescriptor.put(classDescriptor, map);
			displayableDescriptorMap = map;
		}
	}

}
