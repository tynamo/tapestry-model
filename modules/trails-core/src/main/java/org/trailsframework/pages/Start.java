package org.trailsframework.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Property;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.services.DescriptorService;

import java.util.ArrayList;
import java.util.List;

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

		return result;
	}

}