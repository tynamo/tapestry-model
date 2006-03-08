package org.trails.descriptor.annotation;

import org.trails.descriptor.CollectionDescriptor;

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
