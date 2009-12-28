package org.tynamo.components;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.util.DisplayNameUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListPageLinkList
{
	@Inject
	private DescriptorService descriptorService;

	@Property
	private TynamoClassDescriptor descriptorIterator;

	@Inject
	private Messages messages;

	public List<TynamoClassDescriptor> getAllDescriptors()
	{
		List<TynamoClassDescriptor> descriptors = descriptorService.getAllDescriptors();

		List<TynamoClassDescriptor> result = new ArrayList<TynamoClassDescriptor>(descriptors.size());

		for (TynamoClassDescriptor descriptor : descriptors)
		{
			if (!descriptor.isHidden())
			{
				result.add(descriptor);
			}
		}

		Collections.sort(result, new Comparator<TynamoClassDescriptor>()
		{
			public int compare(TynamoClassDescriptor o1, TynamoClassDescriptor o2)
			{
				return DisplayNameUtils.getDisplayName(o1, messages).compareTo(DisplayNameUtils.getDisplayName(o2, messages));
			}
		});

		return result;
	}

	public String getListAllLinkMessage()
	{
		return messages.format("org.tynamo.component.listalllink", DisplayNameUtils.getPluralDisplayName(descriptorIterator, messages));
	}

}
