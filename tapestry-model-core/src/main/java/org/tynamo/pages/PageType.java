package org.tynamo.pages;

public enum PageType {

	LIST,
	SHOW,
	EDIT,
	ADD;

	public String getContextKey() {
		return this.name().toLowerCase();
	}
}
