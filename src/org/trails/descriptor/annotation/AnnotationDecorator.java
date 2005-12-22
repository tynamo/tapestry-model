package org.trails.descriptor.annotation;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import ognl.Ognl;

import org.apache.commons.lang.StringUtils;
import org.trails.descriptor.DescriptorDecorator;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;

/**
 * This class uses the annotations on a given class or property to modify its 
 * descriptor
 * @author fus8882
 *
 */
public class AnnotationDecorator implements DescriptorDecorator
{

    public IClassDescriptor decorate(IClassDescriptor descriptor)
    {
        
        Annotation[] classAnnotations = descriptor.getType().getAnnotations();
        IClassDescriptor decoratedDescriptor = (IClassDescriptor)decorateFromAnnotations(descriptor, classAnnotations);
        ArrayList decoratedPropertyDescriptors = new ArrayList();
        for (Iterator iter = descriptor.getPropertyDescriptors().iterator(); iter.hasNext();)
        {
            IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) iter.next();
            IPropertyDescriptor clonedDescriptor = (IPropertyDescriptor)propertyDescriptor.clone();
            try
            {
                Field propertyField = descriptor.getType().getDeclaredField(
                        propertyDescriptor.getName());
                clonedDescriptor = (IPropertyDescriptor)decorateFromAnnotations(clonedDescriptor, propertyField.getAnnotations());
                
            } catch (Exception ex)
            {
                // don't care
            }
            try
            {
                PropertyDescriptor beanPropDescriptor = (PropertyDescriptor)Ognl.getValue("propertyDescriptors.{? name == '" + propertyDescriptor.getName() + "'}[0]",
                    Introspector.getBeanInfo(descriptor.getType()));
                
                Method readMethod = beanPropDescriptor.getReadMethod();
                clonedDescriptor = (IPropertyDescriptor)decorateFromAnnotations(clonedDescriptor, readMethod.getAnnotations());
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                // don't care
            }
            decoratedPropertyDescriptors.add(clonedDescriptor);
        }
        decoratedDescriptor.setPropertyDescriptors(decoratedPropertyDescriptors);
        sortDescriptors(decoratedDescriptor);
        return decoratedDescriptor;
    }

    /**
     * Rearrange the property descriptors by their index
     * @param descriptor
     */
    private void sortDescriptors(IClassDescriptor descriptor)
    {
        ArrayList sortedDescriptors = new ArrayList();
        sortedDescriptors.addAll(descriptor.getPropertyDescriptors());
        for (Iterator iter = descriptor.getPropertyDescriptors().iterator(); iter.hasNext();)
        {
            IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) iter.next();
            if(propertyDescriptor.getIndex() != IPropertyDescriptor.UNDEFINED_INDEX)
            {
                Collections.swap(sortedDescriptors, 
                        propertyDescriptor.getIndex(),
                        sortedDescriptors.indexOf(propertyDescriptor));
            }
        }
        descriptor.setPropertyDescriptors(sortedDescriptors);
        
    }

    private IDescriptor decorateFromAnnotations( IDescriptor descriptor, Annotation[] annotations)
    {
        IDescriptor clonedDescriptor = (IDescriptor)descriptor.clone();
        for (int i=0; i < annotations.length; i++)
        {
            Annotation annotation = annotations[i];
            // If the annotation type itself has a DescriptorAnnotation, it's one of ours
            DescriptorAnnotation handlerAnnotation = annotation.annotationType().getAnnotation(DescriptorAnnotation.class);
            if (handlerAnnotation != null)
            {
                try
                {
                    DescriptorAnnotationHandler handler = handlerAnnotation.value().newInstance();
                    clonedDescriptor = handler.decorateFromAnnotation(annotation, clonedDescriptor);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }            
        }
        return clonedDescriptor;
    }

}
