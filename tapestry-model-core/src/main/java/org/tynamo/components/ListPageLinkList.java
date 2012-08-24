package org.tynamo.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.util.DisplayNameUtils;
import org.tynamo.util.Utils;

import java.util.Comparator;
import java.util.List;

public class ListPageLinkList
{
	@Inject
	private DescriptorService descriptorService;

	@Inject
	private Messages messages;

	@Parameter(required = true, allowNull = false, defaultPrefix = "literal")
	@Property
	private String listPageName;

	@Property
	private TynamoClassDescriptor descriptorIterator;

	public List<TynamoClassDescriptor> getDisplayableDescriptors()
	{
		return F.flow(descriptorService.getAllDescriptors()).filter(new Predicate<TynamoClassDescriptor>()
		{
			public boolean accept(TynamoClassDescriptor classDescriptor)
			{
				return !classDescriptor.isNonVisual();
			}
		}).sort(new Comparator<TynamoClassDescriptor>()
		{
			public int compare(TynamoClassDescriptor o1, TynamoClassDescriptor o2)
			{
				return DisplayNameUtils.getDisplayName(o1, messages).compareTo(DisplayNameUtils.getDisplayName(o2, messages));
			}
		}).toList();
	}

	public String getListAllLinkMessage()
	{
		return messages.format(Utils.LISTALL_LINK_MESSAGE,
				DisplayNameUtils.getPluralDisplayName(descriptorIterator, messages));
	}
}
