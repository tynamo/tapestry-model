package org.trails.descriptor.annotation;

import java.lang.annotation.Annotation;

import org.trails.descriptor.IDescriptorExtension;

public interface DescriptorExtensionAnnotationHandler<T extends Annotation, X extends IDescriptorExtension>
{
	public X decorateFromAnnotation(T annotation, X descriptor);
}
