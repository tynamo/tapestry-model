package org.trails.descriptor.annotation;

import org.trails.descriptor.IDescriptor;

/**
 * 
 * @author fus8882
 *
 * Applies the annotation to a given property.  
 * @see org.trails.descriptor.annotation.AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
 */
public class PropertyDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<PropertyDescriptor,IDescriptor>
{
    /**
     * 
     * @param propertyDescriptorAnno
     * @param descriptor
     * @return
     * @see AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
     */
    public IDescriptor decorateFromAnnotation(PropertyDescriptor propertyDescriptorAnno, 
            IDescriptor descriptor)
    {
        setPropertiesFromAnnotation(propertyDescriptorAnno, descriptor);
        return descriptor;
    }

}
