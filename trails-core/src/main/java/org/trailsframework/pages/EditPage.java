package org.trailsframework.pages;


import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trailsframework.services.PersistenceService;

public class EditPage {

	private static final Logger LOGGER = LoggerFactory.getLogger(EditPage.class);

	@Inject
	private ContextValueEncoder contextValueEncoder;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	@Inject
	private PersistenceService persitenceService;

	private BeanModel beanModel;

	@Property
	private Object bean;

	@Property
	private Class clazz;

	void pageLoaded() {
		System.out.println("Make other changes to _model here.");
		// Make other changes to _model here.
	}


	void onActivate(Class clazz, String id) throws Exception {
		bean = contextValueEncoder.toValue(clazz, id);
		this.clazz = clazz;
		beanModel = beanModelSource.create(clazz, true, messages);

//		BeanModelUtils.modify(_beanModel, null, null, null, null);
	}

	/**
	 * This tells Tapestry to put _personId into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	Object[] onPassivate() {
		return new Object[]{clazz, bean};
	}

	void onSuccess() {
		LOGGER.info("saving....");
		persitenceService.save(bean);
	}

	void cleanupRender() {
		bean = null;
		clazz = null;
		beanModel = null;
	}

	public BeanModel getBeanModel() {
		return beanModel;
	}
}
