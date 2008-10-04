package org.trailsframework.descriptor.annotation;

import java.lang.annotation.Annotation;

import org.trailsframework.descriptor.IDescriptor;

public interface DescriptorAnnotationHandler<T extends Annotation, X extends IDescriptor>
{
	public X decorateFromAnnotation(T annotation, X descriptor);
}
