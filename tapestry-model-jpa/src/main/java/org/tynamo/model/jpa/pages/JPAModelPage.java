package org.tynamo.model.jpa.pages;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.TynamoBeanContext;

import java.lang.annotation.Annotation;

public abstract class JPAModelPage extends org.tynamo.pages.ModelPage
{

	@Inject
	private Environment environment;

	private TynamoClassDescriptor classDescriptor;

	private BeanModel beanModel;

	private Object bean;

	protected void onPrepare()
	{
		pushTynamoBeanContext();
	}

	protected void pushTynamoBeanContext()
	{
		TynamoBeanContext context = new TynamoBeanContext()
		{
			public Class<?> getBeanClass()
			{
				return classDescriptor.getType();

			}

			public <T extends Annotation> T getAnnotation(Class<T> type)
			{
				return getBeanClass().getAnnotation(type);
			}

			public Object getBean()
			{
				return bean;
			}
		};
		environment.push(TynamoBeanContext.class, context);
	}

	protected void activate(Object bean, TynamoClassDescriptor classDescriptor, BeanModel beanModel)
	{
		this.bean = bean;
		this.classDescriptor = classDescriptor;
		this.beanModel = beanModel;
	}

	protected void cleanupRender()
	{
		bean = null;
		classDescriptor = null;
		beanModel = null;
	}

	final void onActivate(Class clazz, String id) throws Exception
	{
		activate(getContextValueEncoder().toValue(clazz, id), getDescriptorService().getClassDescriptor(clazz),
				createBeanModel(clazz));
	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	protected Object[] onPassivate()
	{
		return new Object[]{getClassDescriptor().getType(), getBean()};
	}

	public final TynamoClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}

	public final BeanModel getBeanModel()
	{
		return beanModel;
	}

	public final Object getBean()
	{
		return bean;
	}

	public final void setBean(Object bean)
	{
		this.bean = bean;
	}

	public boolean isAllowRemove()
	{
		return getClassDescriptor().isAllowRemove() && !getClassDescriptor().isChild();
	}

}
