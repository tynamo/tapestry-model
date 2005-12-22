package org.trails.descriptor.annotation;

import java.lang.annotation.Annotation;

import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IDescriptor;

public class CollectionDecorator implements DescriptorAnnotationHandler<Collection,CollectionDescriptor>
{

    public CollectionDecorator()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public CollectionDescriptor decorateFromAnnotation(Collection annotation, CollectionDescriptor descriptor)
    {
        descriptor.setChildRelationship(annotation.child());
        return descriptor;
    }



}
