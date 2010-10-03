package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.internal.InternalConstants;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.ShowPageBeanModel;


public class ShowPageBeanModelAnnotationHandler extends BeanModelAnnotationHandler
		implements DescriptorAnnotationHandler<ShowPageBeanModel, TynamoClassDescriptor>
{
	public TynamoClassDescriptor decorateFromAnnotation(ShowPageBeanModel annotation, TynamoClassDescriptor descriptor)
	{
		configureBeanModelExtension(descriptor, InternalConstants.SHOW_PAGE_CONTEXT_KEY, annotation.exclude(),
				annotation.include(), annotation.reorder());
		return descriptor;
	}

}
