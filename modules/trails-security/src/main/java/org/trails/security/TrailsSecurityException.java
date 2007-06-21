package org.trails.security;

import ognl.OgnlException;

import org.trails.TrailsRuntimeException;

public class TrailsSecurityException extends TrailsRuntimeException {
	public TrailsSecurityException(String string) {
		super(string);
	}

	public TrailsSecurityException(String string, OgnlException e) {
		super(string, e);
	}
}
