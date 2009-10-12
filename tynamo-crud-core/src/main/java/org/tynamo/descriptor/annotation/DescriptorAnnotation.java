package org.tynamo.descriptor.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DescriptorAnnotation
{
	Class<? extends DescriptorAnnotationHandler> value();
}
