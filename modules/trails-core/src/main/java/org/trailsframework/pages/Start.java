package org.trailsframework.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.services.DescriptorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Start page of application APP.
 */
public class Start
{

	@Inject
	private DescriptorService descriptorService;

	@Property
	private IClassDescriptor descriptorIterator;

}