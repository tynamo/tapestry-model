package org.tynamo.descriptor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.HandledBy;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@HandledBy("ClassDescriptorAnnotationHandler")
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface ClassDescriptor
{
	public static final boolean DEFAULT_nonVisual = false;

	boolean nonVisual() default false;

	boolean searchable() default true;

	boolean hasCyclicRelationships() default false;

}
