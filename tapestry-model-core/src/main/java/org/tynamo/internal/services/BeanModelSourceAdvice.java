package org.tynamo.internal.services;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.services.Environment;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.BeanModelSourceContext;
import org.tynamo.services.DescriptorService;
import org.tynamo.util.BeanModelUtils;

public class BeanModelSourceAdvice implements MethodAdvice
{

	DescriptorService descriptorService;
	Environment environment;

	public BeanModelSourceAdvice(DescriptorService descriptorService, Environment environment)
	{
		this.descriptorService = descriptorService;
		this.environment = environment;
	}

	public void advise(Invocation invocation)
	{

		invocation.proceed();

		if (BeanModel.class.isAssignableFrom(invocation.getResultType()))
		{

			BeanModel<?> dataModel = (BeanModel) invocation.getResult();
			Class<?> beanClass = (Class<?>) invocation.getParameter(0);

			TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanClass);
			BeanModelSourceContext context = environment.peek(BeanModelSourceContext.class);

			if (classDescriptor != null && context != null && StringUtils.isNotEmpty(context.getKey()))
			{
				BeanModelUtils.modify(dataModel, classDescriptor, context.getKey());
				invocation.overrideResult(dataModel);
			}
		}
	}
}
