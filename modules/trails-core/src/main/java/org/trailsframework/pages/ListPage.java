package org.trailsframework.pages;

import org.trailsframework.model.Caetg;
import org.trailsframework.model.HibEntity;
import org.trailsframework.model.MyDomainObject;
import org.trailsframework.services.PersitenceService;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class ListPage
{

	@Inject
	private PersitenceService persitenceService;

	@Property
	private HibEntity model;

	@Property
	private Class clazz;

	void onActivate(Class clazz) throws Exception {
		this.clazz = clazz;
	}

	public List getInstances()
	{
		return persitenceService.getAllInstances(clazz);
	}

	public java.util.List<Caetg> getCaetgs()
	{
		return persitenceService.getAllInstances(Caetg.class);
	}

	public Object[] getEditPageContext()
	{
		return new Object[]{clazz, model};
	}

}
