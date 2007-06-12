package org.trails.security;

public class RoleRequired extends EntitySecurityException {
	public RoleRequired(Object entity, String arg0) {
		super(entity, arg0);
	}

}
