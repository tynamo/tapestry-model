package org.trails.security;

import ognl.OgnlException;

import org.acegisecurity.AcegiSecurityException;

public class TrailsSecurityException extends AcegiSecurityException {
	public TrailsSecurityException(String arg0) {
		super(arg0);
	}

	public TrailsSecurityException(String string, OgnlException e) {
		super(string, e);
	}

}
