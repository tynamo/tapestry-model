package org.trailsframework.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.services.PersistenceService;
import org.trailsframework.util.Identifiable;

import java.util.List;

public class ListPage
{

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Property
	private Identifiable model;

	@Property(write = false)
	private IClassDescriptor classDescriptor;

	void onActivate(Class clazz) throws Exception
	{
		classDescriptor = descriptorService.getClassDescriptor(clazz);
	}

	public List getInstances()
	{
		return persitenceService.getInstances(classDescriptor.getType());
	}

	public Object[] getEditPageContext()
	{
		return new Object[]{classDescriptor.getType(), model};
	}

}
