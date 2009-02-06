package org.trailsframework.descriptor.annotation;

import org.trailsframework.descriptor.TrailsPropertyDescriptor;

/**
 * @author fus8882
 *         <p/>
 *         Applies the annotation to a given property.
 * @see org.trailsframework.descriptor.annotation.AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
 */
public class PropertyDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<PropertyDescriptor, TrailsPropertyDescriptor>
{
	/**
	 * @param propertyDescriptorAnno
	 * @param descriptor
	 * @return
	 * @see AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
	 */
	public TrailsPropertyDescriptor decorateFromAnnotation(PropertyDescriptor propertyDescriptorAnno,
													  TrailsPropertyDescriptor descriptor)
	{
		setPropertiesFromAnnotation(propertyDescriptorAnno, descriptor);
		return descriptor;
	}

}
