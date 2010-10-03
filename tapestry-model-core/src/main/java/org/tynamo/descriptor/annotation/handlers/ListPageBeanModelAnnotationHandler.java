package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.internal.InternalConstants;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.ListPageBeanModel;


public class ListPageBeanModelAnnotationHandler extends BeanModelAnnotationHandler
		implements DescriptorAnnotationHandler<ListPageBeanModel, TynamoClassDescriptor>
{
	public TynamoClassDescriptor decorateFromAnnotation(ListPageBeanModel annotation, TynamoClassDescriptor descriptor)
	{
		configureBeanModelExtension(descriptor, InternalConstants.LIST_PAGE_CONTEXT_KEY, annotation.exclude(),
				annotation.include(), annotation.reorder());
		return descriptor;
	}

}
