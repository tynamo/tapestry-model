package org.tynamo.descriptor.annotation;

import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@HandledBy("MethodDescriptorAnnotationHandler")
public @interface MethodDescriptor
{

	public static final boolean DEFAULT_nonVisual = false;
	public static final String DEFAULT_displayName = "";

	boolean nonVisual() default false;

	String displayName() default "";
}
