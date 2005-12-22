package org.trails.descriptor.annotation;

import java.lang.annotation.Annotation;

import org.trails.descriptor.IClassDescriptor;

public class ClassDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<ClassDescriptor,IClassDescriptor>
{

    public ClassDescriptorAnnotationHandler()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public IClassDescriptor decorateFromAnnotation(ClassDescriptor annotation, IClassDescriptor descriptor)
    {
        setPropertiesFromAnnotation(annotation, descriptor);
        return descriptor;
    }


}
