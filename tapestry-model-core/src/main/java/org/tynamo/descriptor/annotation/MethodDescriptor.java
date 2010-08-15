package org.tynamo.descriptor.annotation;

import org.tynamo.descriptor.annotation.handlers.MethodDescriptorAnnotationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@HandledBy(MethodDescriptorAnnotationHandler.class)
public @interface MethodDescriptor
{

	public static final String DEFAULT_displayName = "";

	boolean hidden() default false;

	String displayName() default "";
}
