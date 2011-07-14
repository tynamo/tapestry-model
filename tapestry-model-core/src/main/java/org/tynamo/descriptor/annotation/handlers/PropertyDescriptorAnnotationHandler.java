package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.annotation.PropertyDescriptor;

/**
 * Applies the annotation to a given property.
 */
public class PropertyDescriptorAnnotationHandler implements DescriptorAnnotationHandler<PropertyDescriptor, TynamoPropertyDescriptor>
{
	/**
	 * @param propertyDescriptorAnno
	 * @param descriptor
	 */
	public void decorateFromAnnotation(PropertyDescriptor propertyDescriptorAnno,
													  TynamoPropertyDescriptor descriptor)
	{
		AnnotationHandlerUtils.setPropertiesFromAnnotation(propertyDescriptorAnno, descriptor);
	}

}
