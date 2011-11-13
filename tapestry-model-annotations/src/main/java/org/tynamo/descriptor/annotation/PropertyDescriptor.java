package org.tynamo.descriptor.annotation;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@HandledBy("PropertyDescriptorAnnotationHandler")
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface PropertyDescriptor
{

	public static final String DEFAULT_format = "no_format";

	/**
	 * Specifies if a property should appear on both edit and list pages
	 *
	 * @return
	 * @see org.tynamo.descriptor.Descriptor#isNonVisual()
	 */
	boolean nonVisual() default false;


	boolean readOnly() default false;

	/**
	 * Specifies if property should appear on search pages
	 *
	 * @return
	 */
	boolean searchable() default true;

	/**
	 * Specifies if property can contain html.
	 *
	 * @return
	 */
	boolean richText() default false;

	/**
	 * A format pattern string
	 *
	 * @return
	 * @see java.text.SimpleDateFormat
	 * @see java.text.NumberFormat
	 */
	String format() default DEFAULT_format;

}
