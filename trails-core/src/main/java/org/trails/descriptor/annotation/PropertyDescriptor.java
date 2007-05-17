package org.trails.descriptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trails.descriptor.IPropertyDescriptor;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@DescriptorAnnotation(PropertyDescriptorAnnotationHandler.class)
public @interface PropertyDescriptor
{
	public static final String DEFAULT_displayName = "";
	public static final int DEFAULT_index = IPropertyDescriptor.UNDEFINED_INDEX;
	public static final String DEFAULT_format = "no_format";

	/**
	 * Specifies if a property should appear list page.
	 *
	 * @return
	 */
	boolean summary() default true;

	/**
	 * Specifies if a property should appear on both edit and list pages
	 *
	 * @return
	 * @see org.trails.descriptor.IDescriptor#isHidden()
	 */
	boolean hidden() default false;


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
	 * Override the default label text
	 *
	 * @return
	 */
	String displayName() default "";

	String shortDescription() default "";

	/**
	 * A format pattern string
	 *
	 * @return
	 * @see java.text.SimpleDateFormat
	 * @see java.text.NumberFormat
	 */
	String format() default "no_format";

	int index() default IPropertyDescriptor.UNDEFINED_INDEX;
}
