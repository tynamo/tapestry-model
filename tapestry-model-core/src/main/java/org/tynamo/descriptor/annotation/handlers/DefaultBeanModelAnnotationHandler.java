package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.internal.InternalConstants;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.DefaultBeanModel;


public class DefaultBeanModelAnnotationHandler extends BeanModelAnnotationHandler
		implements DescriptorAnnotationHandler<DefaultBeanModel, TynamoClassDescriptor>
{
	public TynamoClassDescriptor decorateFromAnnotation(DefaultBeanModel annotation, TynamoClassDescriptor descriptor)
	{
		configureBeanModelExtension(descriptor, InternalConstants.DEFAULT_BEANMODEL_CONTEXT_KEY, annotation.exclude(),
				annotation.include(), annotation.reorder());
		return descriptor;
	}

}
