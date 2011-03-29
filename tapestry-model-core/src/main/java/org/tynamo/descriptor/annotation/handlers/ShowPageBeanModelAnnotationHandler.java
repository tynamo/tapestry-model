package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.pages.PageType;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.ShowPageBeanModel;


public class ShowPageBeanModelAnnotationHandler extends BeanModelAnnotationHandler
		implements DescriptorAnnotationHandler<ShowPageBeanModel, TynamoClassDescriptor>
{
	public TynamoClassDescriptor decorateFromAnnotation(ShowPageBeanModel annotation, TynamoClassDescriptor descriptor)
	{
		configureBeanModelExtension(descriptor, PageType.SHOW.getContextKey(), annotation.exclude(),
				annotation.include(), annotation.reorder());
		return descriptor;
	}

}
