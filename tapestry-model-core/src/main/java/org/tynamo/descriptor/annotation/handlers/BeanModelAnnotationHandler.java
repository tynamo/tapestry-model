package org.tynamo.descriptor.annotation.handlers;

import org.apache.commons.lang.StringUtils;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.extension.BeanModelExtension;


public abstract class BeanModelAnnotationHandler
{

	protected void configureBeanModelExtension(TynamoClassDescriptor descriptor, String contextKey, String exclude,
	                                           String include, String reorder)
	{
		BeanModelExtension beanModelExtension = BeanModelExtension.obtainBeanModelExtension(descriptor);

		if (StringUtils.isNotEmpty(exclude)) beanModelExtension.setExcludePropertyNames(contextKey, exclude);
		if (StringUtils.isNotEmpty(include)) beanModelExtension.setIncludePropertyNames(contextKey, include);
		if (StringUtils.isNotEmpty(reorder)) beanModelExtension.setReorderPropertyNames(contextKey, reorder);
	}
}
