package org.trailsframework.examples.recipe.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Property;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.services.DescriptorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * Start page of application APP.
 */
public class Start {

	@Inject
	private DescriptorService descriptorService;

	@Property
	private IClassDescriptor descriptorIterator;


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
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});

		return result;
	}

}