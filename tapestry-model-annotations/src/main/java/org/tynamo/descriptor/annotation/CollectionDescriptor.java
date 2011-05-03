package org.tynamo.descriptor.annotation;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@HandledBy("CollectionDescriptorAnnotationHandler")
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface CollectionDescriptor
{

	public static final String DEFAULT_inverse = "";
	public static final String DEFAULT_addExpression = "";
	public static final String DEFAULT_removeExpression = "";
	public static final String DEFAULT_swapExpression = "";

	/**
	 * The field in the other end of the realtionship. Required for @OneToMany @CollectionDescriptor(child=false)
	 *
	 * @return
	 */
	public String inverse() default "";

	/**
	 * Child collections will not allow you to choose from all possible instances of the element type, only to create new
	 * instances and remove instances from the collection.
	 *
	 * @return
	 */
	public boolean child() default false;

	public String addExpression() default "";

	public String removeExpression() default "";

	public String swapExpression() default "";

	public boolean allowRemove() default true;

}
