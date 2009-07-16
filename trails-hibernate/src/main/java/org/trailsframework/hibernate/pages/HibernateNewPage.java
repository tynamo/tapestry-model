package org.trailsframework.hibernate.pages;


import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trailsframework.builder.BuilderDirector;

public abstract class HibernateNewPage extends HibernateEditPage
{

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateNewPage.class);

	@Inject
	private BuilderDirector builderDirector;

	void onActivate(Class clazz) throws Exception
	{
		activate(builderDirector.createNewInstance(clazz),
				getDescriptorService().getClassDescriptor(clazz),
				getBeanModelSource().create(clazz, true, getMessages()));
//		BeanModelUtils.modify(_beanModel, null, null, null, null);
	}

	@Override
	protected Object[] onPassivate()
	{
		return new Object[]{getClassDescriptor().getType()};
	}

}