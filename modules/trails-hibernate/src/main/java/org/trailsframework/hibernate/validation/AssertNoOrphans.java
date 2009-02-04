package org.trailsframework.hibernate.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface AssertNoOrphans {
	// we can't default this to null, which sucks!

	Class value() default Object.class;

	String childrenProperty() default "";

	String message() default "";
}
