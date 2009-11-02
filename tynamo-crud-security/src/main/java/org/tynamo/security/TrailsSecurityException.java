package org.tynamo.security;

import ognl.OgnlException;

import org.tynamo.exception.TrailsRuntimeException;

public class TrailsSecurityException extends TrailsRuntimeException {
	public TrailsSecurityException(String string) {
		super(string);
	}

	public TrailsSecurityException(String string, OgnlException e) {
		super(string, e);
	}
}
