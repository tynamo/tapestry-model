package org.trails.descriptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@DescriptorAnnotation(CollectionDecorator.class)
public @interface Collection
{

	public static final String DEFAULT_inverse = "";

	/**
	 * The field in the other end of the realtionship.
	 * Required for @OneToMany @Collection(child=false)
	 *
	 * @return
	 */
	public String inverse() default "";

	/**
	 * Child collections will not allow you to choose from
	 * all possible instances of the element type, only to create new
	 * instances and remove instances from the collection.
	 *
	 * @return
	 */
	public boolean child() default false;
}
