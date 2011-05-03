package org.tynamo;

public enum PageType {

	DEFAULT,
	LIST,
	SHOW,
	EDIT,
	ADD;

	public String getContextKey() {
		return this.name().toLowerCase();
	}
}
