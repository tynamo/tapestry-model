package org.tynamo.descriptor.annotation;

import org.tynamo.descriptor.annotation.handlers.ClassDescriptorAnnotationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@DescriptorAnnotation(ClassDescriptorAnnotationHandler.class)
public @interface ClassDescriptor
{
	public static final boolean DEFAULT_hidden = false;

	boolean hidden() default false;

	boolean hasCyclicRelationships() default false;

}
