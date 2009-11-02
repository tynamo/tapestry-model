package org.tynamo.hibernate.pages;


import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.builder.BuilderDirector;

public abstract class HibernateNewPage extends HibernateEditPage
{

	@Inject
	private BuilderDirector builderDirector;

	protected void onActivate(Class clazz) throws Exception
	{
		activate(builderDirector.createNewInstance(clazz), getDescriptorService().getClassDescriptor(clazz), createBeanModel(clazz));
	}

	@Override
	protected Object[] onPassivate()
	{
		return new Object[]{getClassDescriptor().getType()};
	}

}