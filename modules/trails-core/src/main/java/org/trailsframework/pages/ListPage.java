package org.trailsframework.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.trailsframework.services.PersistenceService;

import java.util.List;

public class ListPage
{

	@Inject
	private PersistenceService persitenceService;

	@Property
	private Object model;

	@Property
	private Class clazz;

	void onActivate(Class clazz) throws Exception
	{
		this.clazz = clazz;
	}

	public List getInstances()
	{
		return persitenceService.getInstances(clazz);
	}

	public Object[] getEditPageContext()
	{
		return new Object[]{clazz, model};
	}

}
