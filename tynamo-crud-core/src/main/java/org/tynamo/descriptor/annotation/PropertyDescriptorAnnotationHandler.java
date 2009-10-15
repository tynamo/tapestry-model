package org.tynamo.descriptor.annotation;

import org.tynamo.descriptor.TynamoPropertyDescriptor;

/**
 * @author fus8882
 *         <p/>
 *         Applies the annotation to a given property.
 * @see org.tynamo.descriptor.annotation.AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
 */
public class PropertyDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<PropertyDescriptor, TynamoPropertyDescriptor>
{
	/**
	 * @param propertyDescriptorAnno
	 * @param descriptor
	 * @return
	 * @see AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
	 */
	public TynamoPropertyDescriptor decorateFromAnnotation(PropertyDescriptor propertyDescriptorAnno,
													  TynamoPropertyDescriptor descriptor)
	{
		setPropertiesFromAnnotation(propertyDescriptorAnno, descriptor);
		return descriptor;
	}

}
