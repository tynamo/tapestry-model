package org.trails.descriptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@DescriptorAnnotation(ClassDescriptorAnnotationHandler.class)
public @interface ClassDescriptor
{
    public static final String DEFAULT_displayName = "";
    String displayName() default "";
    String pluralDisplayName() default "";
}
