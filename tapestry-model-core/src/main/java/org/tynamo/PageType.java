package org.tynamo;

public enum PageType {

	LIST,
	SHOW,
	EDIT,
	ADD;

	public String getContextKey() {
		return this.name().toLowerCase();
	}
}
