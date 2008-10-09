package org.trailsframework.examples.recipe.pages;


import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trailsframework.builder.BuilderDirector;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.services.PersistenceService;

public class NewPage
{

	private static final Logger LOGGER = LoggerFactory.getLogger(NewPage.class);

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private BuilderDirector builderDirector;

	@Property(write = false)
	private IClassDescriptor classDescriptor;

	@Property(write = false)
	private BeanModel beanModel;

	@Property(write = false)
	private Object bean;

	void pageLoaded()
	{
		// Make other changes to _model here.
	}

	void onActivate(Class clazz) throws Exception
	{
		bean = builderDirector.createNewInstance(clazz);
		classDescriptor = descriptorService.getClassDescriptor(clazz);
		beanModel = beanModelSource.create(clazz, true, messages);

//		BeanModelUtils.modify(_beanModel, null, null, null, null);
	}

	/**
	 * This tells Tapestry to put _personId into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	Object[] onPassivate()
	{
		return new Object[]{classDescriptor.getType()};
	}

	Class onSuccess()
	{
		LOGGER.info("saving....");
		persitenceService.save(bean);
		return Start.class;
	}

	void cleanupRender()
	{
		bean = null;
		classDescriptor = null;
		beanModel = null;
	}

}