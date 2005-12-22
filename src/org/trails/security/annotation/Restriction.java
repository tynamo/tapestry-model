package org.trails.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trails.security.RestrictionType;

@Retention(RetentionPolicy.RUNTIME)
public @interface Restriction
{
    RestrictionType restrictionType();
    String requiredRole();
}
