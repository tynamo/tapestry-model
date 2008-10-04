package org.trailsframework.descriptor.annotation;

import org.trailsframework.descriptor.IPropertyDescriptor;

/**
 * @author fus8882
 *         <p/>
 *         Applies the annotation to a given property.
 * @see org.trailsframework.descriptor.annotation.AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
 */
public class PropertyDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<PropertyDescriptor, IPropertyDescriptor>
{
	/**
	 * @param propertyDescriptorAnno
	 * @param descriptor
	 * @return
	 * @see AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
	 */
	public IPropertyDescriptor decorateFromAnnotation(PropertyDescriptor propertyDescriptorAnno,
													  IPropertyDescriptor descriptor)
	{
		setPropertiesFromAnnotation(propertyDescriptorAnno, descriptor);
		return descriptor;
	}

}
