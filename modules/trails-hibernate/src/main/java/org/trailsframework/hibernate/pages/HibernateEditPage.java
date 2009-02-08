package org.trailsframework.hibernate.pages;


import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trailsframework.descriptor.TrailsClassDescriptor;

public abstract class HibernateEditPage extends HibernateModelPage
{

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateEditPage.class);

	private TrailsClassDescriptor classDescriptor;

	@Property(write = false)
	private BeanModel beanModel;

	private Object bean;

	public TrailsClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}

	public Object getBean()
	{
		return bean;
	}

	void pageLoaded()
	{
		// Make other changes to the bean here.
	}

	void onActivate(Class clazz, String id) throws Exception
	{
		bean = getContextValueEncoder().toValue(clazz, id);
		classDescriptor = getDescriptorService().getClassDescriptor(clazz);
		beanModel = getBeanModelSource().create(clazz, true, getMessages());
//		BeanModelUtils.modify(_beanModel, null, null, null, null);
	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	Object[] onPassivate()
	{
		return new Object[]{classDescriptor.getType(), bean};
	}


	void cleanupRender()
	{
		bean = null;
		classDescriptor = null;
		beanModel = null;
	}

	public Link onActionFromDelete()
	{
		getPersitenceService().remove(bean);
		return back();
	}

}
