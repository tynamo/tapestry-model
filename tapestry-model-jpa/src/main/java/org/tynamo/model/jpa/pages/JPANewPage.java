package org.tynamo.model.jpa.pages;


import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.builder.BuilderDirector;
import org.tynamo.util.Utils;

public abstract class JPANewPage extends JPAEditPage
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

	@Override
	protected String getSuccessMessageKey()
	{
		return Utils.ADDED_MESSAGE;
	}

}