package org.trails.descriptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
@DescriptorAnnotation(HardOneToOneAnnotationHandler.class)
public @interface HardOneToOne {

	public enum Identity {OWNER, ASSOCIATION}

	public Identity identity() default Identity.ASSOCIATION;
}