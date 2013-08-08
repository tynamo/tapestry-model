package org.tynamo.descriptor.annotation.handlers;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.PageType;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.BeanModel;
import org.tynamo.descriptor.annotation.beaneditor.BeanModels;
import org.tynamo.descriptor.extension.BeanModelExtension;
import org.tynamo.mixins.BeanModelAdvisor;


public class BeanModelAnnotationHandler implements DescriptorAnnotationHandler<BeanModels, TynamoClassDescriptor>
{
	private final static Logger logger = LoggerFactory.getLogger(BeanModelAdvisor.class);

	public void decorateFromAnnotation(final BeanModels annotation, final TynamoClassDescriptor descriptor)
	{

		BeanModelExtension beanModelExtension = BeanModelExtension.obtainBeanModelExtension(descriptor);

		for (BeanModel beanModel : annotation.value())
		{
			boolean thereIsAKey = StringUtils.isNotEmpty(beanModel.key());

			if (thereIsAKey && beanModel.pageType() != PageType.DEFAULT)
				logger.warn("{}@BeanModel.pageType is ignored. Reason: @BeanModel.pageType value is ignored if the @BeanModel.key is specified", descriptor.getBeanType().getSimpleName());
			if (beanModel.beanType() != void.class)
				logger.warn("{}@BeanModel.beanType is ignored. Reason: @BeanModel.beanType value is ignored when the @BeanModel annotation is used on entities", descriptor.getBeanType().getSimpleName());

			String key = thereIsAKey ? beanModel.key() : beanModel.pageType().getContextKey();

			String exclude = beanModel.exclude();
			String include = beanModel.include();
			String reorder = beanModel.reorder();

			if (StringUtils.isNotEmpty(exclude)) beanModelExtension.setExcludePropertyNames(key, exclude);
			if (StringUtils.isNotEmpty(include)) beanModelExtension.setIncludePropertyNames(key, include);
			if (StringUtils.isNotEmpty(reorder)) beanModelExtension.setReorderPropertyNames(key, reorder);
		}

	}

}
