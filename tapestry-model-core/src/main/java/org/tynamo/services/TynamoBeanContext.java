package org.tynamo.services;

import org.apache.tapestry5.internal.BeanValidationContext;

/**
 * Defines a context for editing a bean via {@link org.apache.tapestry5.corelib.components.BeanEditor}.
 * This value is made available at render time via the {@link org.apache.tapestry5.annotations.Environmental} annotation.
 */
public interface TynamoBeanContext extends BeanValidationContext
{

}
