package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.internal.InternalConstants;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.AddPageBeanModel;


public class AddPageBeanModelAnnotationHandler extends BeanModelAnnotationHandler
		implements DescriptorAnnotationHandler<AddPageBeanModel, TynamoClassDescriptor>
{
	public TynamoClassDescriptor decorateFromAnnotation(AddPageBeanModel annotation, TynamoClassDescriptor descriptor)
	{
		configureBeanModelExtension(descriptor, InternalConstants.ADD_PAGE_CONTEXT_KEY, annotation.exclude(),
				annotation.include(), annotation.reorder());
		return descriptor;
	}

}
