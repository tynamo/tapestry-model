package org.tynamo.components;

import java.util.Map.Entry;
import java.util.SortedMap;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterPredicate;

public class SearchFilters
{
	@Property
	private Entry<TynamoPropertyDescriptor, Object> entry;

	@Parameter(required = true, allowNull = false, autoconnect = true)
	@Property(write = false)
	private SortedMap<TynamoPropertyDescriptor, SearchFilterPredicate> descriptorMap;

}
