package org.trails.security;

import ognl.OgnlException;

public class EntitySecurityException extends TrailsSecurityException {
	private Object entity;
	public Object getEntity() {
		return entity;
	}

	public EntitySecurityException(Object entity, String arg0) {
		this(entity, arg0, null);
	}

	public EntitySecurityException(Object entity, String string, OgnlException e) {
		super(string, e);
		this.entity = entity;
	}

}
