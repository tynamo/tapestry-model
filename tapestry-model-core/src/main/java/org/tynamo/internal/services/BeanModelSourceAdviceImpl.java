package org.tynamo.internal.services;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.services.Environment;
import org.tynamo.services.BeanModelSourceContext;

public class BeanModelSourceAdviceImpl implements BeanModelSourceAdvice
{

	private final BeanModelsAnnotationBMModifier beanModelProvider;
	private final Environment environment;

	public BeanModelSourceAdviceImpl(BeanModelsAnnotationBMModifier beanModelProvider, Environment environment)
	{
		this.beanModelProvider = beanModelProvider;
		this.environment = environment;
	}

	public void advise(MethodInvocation invocation)
	{
		invocation.proceed();

		if (BeanModel.class.isAssignableFrom(invocation.getMethod().getReturnType()))
		{
			BeanModel<?> dataModel = (BeanModel) invocation.getReturnValue();
			BeanModelSourceContext context = environment.peek(BeanModelSourceContext.class);

			if (context != null && StringUtils.isNotEmpty(context.getKey()))
			{
				beanModelProvider.modify(dataModel, context.getKey());
				invocation.setReturnValue(dataModel);
			}
		}
	}
}
