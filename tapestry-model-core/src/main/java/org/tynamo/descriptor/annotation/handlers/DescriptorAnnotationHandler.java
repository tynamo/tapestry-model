package org.tynamo.descriptor.annotation.handlers;

import java.lang.annotation.Annotation;

import org.tynamo.descriptor.Descriptor;

public interface DescriptorAnnotationHandler<T extends Annotation, X extends Descriptor>
{
	public X decorateFromAnnotation(T annotation, X descriptor);
}
