package org.trails.descriptor.annotation;

import org.trails.descriptor.CollectionDescriptor;

public class CollectionDescriptorAnnotationHandler implements DescriptorAnnotationHandler<Collection, CollectionDescriptor>
{

	public CollectionDescriptor decorateFromAnnotation(Collection annotation, CollectionDescriptor descriptor)
	{
		descriptor.setChildRelationship(annotation.child());
		if (!Collection.DEFAULT_inverse.equals(annotation.inverse()))
		{
			descriptor.setInverseProperty(annotation.inverse());
		}
		return descriptor;
	}
}
