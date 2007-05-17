package org.trails.descriptor.annotation;

import java.lang.annotation.Annotation;

import org.trails.descriptor.IDescriptor;

public interface DescriptorAnnotationHandler<T extends Annotation, X extends IDescriptor>
{
	public X decorateFromAnnotation(T annotation, X descriptor);
}
