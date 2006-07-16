package org.trails.security.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.trails.security.RestrictionType;

@Retention(RetentionPolicy.RUNTIME)
public @interface Restriction
{
    RestrictionType restrictionType();
    String requiredRole();
}
