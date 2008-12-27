package org.trailsframework.components;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.util.DisplayNameUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListPageLinkList
{
	@Inject
	private DescriptorService descriptorService;

	@Property
	private IClassDescriptor descriptorIterator;

	@Inject
	private Messages messages;

	public List<IClassDescriptor> getAllDescriptors()
	{
		List<IClassDescriptor> descriptors = descriptorService.getAllDescriptors();

		List<IClassDescriptor> result = new ArrayList<IClassDescriptor>(descriptors.size());

		for (IClassDescriptor descriptor : descriptors)
		{
			if (!descriptor.isHidden())
			{
				result.add(descriptor);
			}
		}

		Collections.sort(result, new Comparator<IClassDescriptor>()
		{
			public int compare(IClassDescriptor o1, IClassDescriptor o2)
			{
				return DisplayNameUtils.getDisplayName(o1, messages).compareTo(DisplayNameUtils.getDisplayName(o2, messages));
			}
		});

		return result;
	}

	public String getListAllLinkMessage()
	{
		return messages.format("org.trails.component.listalllink", DisplayNameUtils.getPluralDisplayName(descriptorIterator, messages));
	}

}
