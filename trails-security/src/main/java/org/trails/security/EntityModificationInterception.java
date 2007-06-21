package org.trails.security;

public class EntityModificationInterception extends TrailsSecurityException {
	private Object entity;
	public Object getEntity() {
		return entity;
	}

	public EntityModificationInterception(Object entity, String string) {
		super(string);
		this.entity = entity;
	}
}
