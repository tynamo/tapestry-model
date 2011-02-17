package org.tynamo.descriptor.annotation;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@HandledBy("ClassDescriptorAnnotationHandler")
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface ClassDescriptor
{
	public static final boolean DEFAULT_nonVisual = false;

	boolean nonVisual() default false;

	boolean hasCyclicRelationships() default false;

}
