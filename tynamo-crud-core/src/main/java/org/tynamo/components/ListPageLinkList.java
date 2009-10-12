package org.tynamo.components;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.descriptor.TrailsClassDescriptor;
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
	private TrailsClassDescriptor descriptorIterator;

	@Inject
	private Messages messages;

	public List<TrailsClassDescriptor> getAllDescriptors()
	{
		List<TrailsClassDescriptor> descriptors = descriptorService.getAllDescriptors();

		List<TrailsClassDescriptor> result = new ArrayList<TrailsClassDescriptor>(descriptors.size());

		for (TrailsClassDescriptor descriptor : descriptors)
		{
			if (!descriptor.isHidden())
			{
				result.add(descriptor);
			}
		}

		Collections.sort(result, new Comparator<TrailsClassDescriptor>()
		{
			public int compare(TrailsClassDescriptor o1, TrailsClassDescriptor o2)
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
