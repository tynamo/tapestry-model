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

		BeanModelExtension beanModelExtension = BeanModelExtension.obtainBeanModelExtension(descriptor);
		beanModelExtension.setApplyDefaultExclusions(annotation.applyDefaultExclusions());

		for (BeanModel beanModel : annotation.value())
		{
			String context = StringUtils.isNotEmpty(beanModel.context()) ? beanModel.context() : beanModel.pageType().getContextKey();

			String exclude = beanModel.exclude();
			String include = beanModel.include();
			String reorder = beanModel.reorder();

			if (StringUtils.isNotEmpty(exclude)) beanModelExtension.setExcludePropertyNames(context, exclude);
			if (StringUtils.isNotEmpty(include)) beanModelExtension.setIncludePropertyNames(context, include);
			if (StringUtils.isNotEmpty(reorder)) beanModelExtension.setReorderPropertyNames(context, reorder);
		}

		return descriptor;
	}

}
