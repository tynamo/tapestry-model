package org.trailsframework.descriptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@DescriptorAnnotation(MethodDescriptorAnnotationHandler.class)
public @interface MethodDescriptor
{

	public static final String DEFAULT_displayName = "";

	boolean hidden() default false;

	String displayName() default "";
}
