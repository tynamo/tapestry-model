package org.trailsframework.descriptor.annotation;

import java.lang.annotation.Annotation;

import org.trailsframework.descriptor.Descriptor;

public interface DescriptorAnnotationHandler<T extends Annotation, X extends Descriptor>
{
	public X decorateFromAnnotation(T annotation, X descriptor);
}
