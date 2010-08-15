package org.tynamo.descriptor.annotation;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.ClassDescriptorAnnotationHandler;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@HandledBy(ClassDescriptorAnnotationHandler.class)
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface ClassDescriptor
{
	public static final boolean DEFAULT_hidden = false;

	boolean hidden() default false;

	boolean hasCyclicRelationships() default false;

}
