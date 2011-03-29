package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.PageType;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.AddPageBeanModel;


public class AddPageBeanModelAnnotationHandler extends BeanModelAnnotationHandler
		implements DescriptorAnnotationHandler<AddPageBeanModel, TynamoClassDescriptor>
{
	public TynamoClassDescriptor decorateFromAnnotation(AddPageBeanModel annotation, TynamoClassDescriptor descriptor)
	{
		configureBeanModelExtension(descriptor, PageType.ADD.getContextKey(), annotation.exclude(),
				annotation.include(), annotation.reorder());
		return descriptor;
	}

}
