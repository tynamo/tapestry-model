package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.PageType;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.EditPageBeanModel;


public class EditPageBeanModelAnnotationHandler extends BeanModelAnnotationHandler
		implements DescriptorAnnotationHandler<EditPageBeanModel, TynamoClassDescriptor>
{
	public TynamoClassDescriptor decorateFromAnnotation(EditPageBeanModel annotation, TynamoClassDescriptor descriptor)
	{
		configureBeanModelExtension(descriptor, PageType.EDIT.getContextKey(), annotation.exclude(),
				annotation.include(), annotation.reorder());
		return descriptor;
	}

}
