package org.trails.descriptor.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.trails.descriptor.DescriptorDecorator;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

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
