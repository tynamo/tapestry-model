package org.tynamo.internal.services;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.services.RequestGlobals;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.extension.BeanModelExtension;
import org.tynamo.services.DescriptorService;

public class BeanModelSourceAdvice implements MethodAdvice {

	DescriptorService descriptorService;
	RequestGlobals requestGlobals;

	public BeanModelSourceAdvice(DescriptorService descriptorService,
	                             RequestGlobals requestGlobals) {
		this.descriptorService = descriptorService;
		this.requestGlobals = requestGlobals;
	}

	public void advise(Invocation invocation) {

		invocation.proceed();

		if (BeanModel.class.isAssignableFrom(invocation.getResultType())) {

			BeanModel<?> dataModel = (BeanModel) invocation.getResult();
			Class<?> beanClass = (Class<?>) invocation.getParameter(0);

			TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanClass);

			if (classDescriptor != null) {
				String activePageName = requestGlobals.getActivePageName().toLowerCase();
				int index = activePageName.indexOf(beanClass.getSimpleName().toLowerCase());
				String contextKey = index > 0 ? activePageName.substring(0, index) : activePageName;

				if (classDescriptor.supportsExtension(BeanModelExtension.class)) {
					BeanModelExtension extension = classDescriptor.getExtension(BeanModelExtension.class);
					BeanModelUtils.modify(dataModel, null,
							extension.getIncludePropertyNames(contextKey),
							extension.getExcludePropertyNames(contextKey),
							extension.getReorderPropertyNames(contextKey));
				}
			}
			invocation.overrideResult(dataModel);
		}
	}
}
