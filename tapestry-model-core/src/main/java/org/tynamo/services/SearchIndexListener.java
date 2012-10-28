package org.tynamo.services;

public interface SearchIndexListener {
	public void postPersist(Object entity);

	public void postRemove(Object entity);

	public void startListening();
}