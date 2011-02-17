package org.tynamo.descriptor.annotation.handlers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HandledBy
{
	/**
	 * The id of the service to inject; either a fully qualified id, or the unqualified id of a service within the same
	 * module.
	 */
	String value();
}
