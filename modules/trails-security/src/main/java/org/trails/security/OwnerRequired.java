package org.trails.security;

public class OwnerRequired extends EntitySecurityException {
	public OwnerRequired(Object entity, String arg0) {
		super(entity, arg0);
	}

}
