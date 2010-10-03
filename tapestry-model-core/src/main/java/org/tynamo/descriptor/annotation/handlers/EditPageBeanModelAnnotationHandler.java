package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.internal.InternalConstants;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.EditPageBeanModel;


public class EditPageBeanModelAnnotationHandler extends BeanModelAnnotationHandler
		implements DescriptorAnnotationHandler<EditPageBeanModel, TynamoClassDescriptor>
{
	public TynamoClassDescriptor decorateFromAnnotation(EditPageBeanModel annotation, TynamoClassDescriptor descriptor)
	{
		configureBeanModelExtension(descriptor, InternalConstants.EDIT_PAGE_CONTEXT_KEY, annotation.exclude(),
				annotation.include(), annotation.reorder());
		return descriptor;
	}

}
