package org.tynamo.descriptor.annotation.handlers;

import org.apache.commons.lang.StringUtils;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.BeanModel;
import org.tynamo.descriptor.annotation.beaneditor.BeanModels;
import org.tynamo.descriptor.extension.BeanModelExtension;


public class BeanModelAnnotationHandler implements DescriptorAnnotationHandler<BeanModels, TynamoClassDescriptor>
{

	public TynamoClassDescriptor decorateFromAnnotation(final BeanModels annotation, final TynamoClassDescriptor descriptor)
	{
		for (BeanModel beanModel : annotation.value())
		{
			String context = StringUtils.isNotEmpty(beanModel.context()) ? beanModel.context() : beanModel.pageType().getContextKey();
			configureBeanModelExtension(descriptor, context, beanModel.exclude(), beanModel.include(), beanModel.reorder());
		}

		return descriptor;
	}

	protected void configureBeanModelExtension(TynamoClassDescriptor descriptor, String contextKey, String exclude,
	                                           String include, String reorder)
	{
		BeanModelExtension beanModelExtension = BeanModelExtension.obtainBeanModelExtension(descriptor);

		if (StringUtils.isNotEmpty(exclude)) beanModelExtension.setExcludePropertyNames(contextKey, exclude);
		if (StringUtils.isNotEmpty(include)) beanModelExtension.setIncludePropertyNames(contextKey, include);
		if (StringUtils.isNotEmpty(reorder)) beanModelExtension.setReorderPropertyNames(contextKey, reorder);
	}
}
