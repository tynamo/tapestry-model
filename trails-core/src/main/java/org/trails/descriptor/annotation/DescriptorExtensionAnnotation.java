package org.trails.descriptor.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DescriptorExtensionAnnotation
{
	Class<? extends DescriptorExtensionAnnotationHandler> value();
}
