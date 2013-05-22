package org.tynamo.hibernate.services;

import java.util.List;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.hibernate.criterion.DetachedCriteria;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.hibernate.QueryParameter;
import org.tynamo.services.PersistenceService;

public interface HibernatePersistenceService extends PersistenceService
{

	public <T> T getInstance(Class<T> type, DetachedCriteria criteria);

	public <T> List<T> getInstances(Class<T> type, DetachedCriteria criteria);

	public <T> List<T> getInstances(Class<T> type, DetachedCriteria criteria, int startIndex, int maxResults);

	public int count(Class type, DetachedCriteria criteria);

	/**
	 * @param model to attach to the current persistence session
	 */
	public void reattach(Object model);

	/**
	 * Does a query by example
	 *
	 * @param example
	 * @return
	 */
	public <T> List<T> getInstances(T example, TynamoClassDescriptor classDescriptor);

	public <T> T merge(T instance);

	public <T> T saveOrUpdate(T instance);

	public List findByQuery(String queryString);

	public List findByQuery(String queryString, QueryParameter... parameters);

	public List findByQuery(String queryString, int startIndex, int maxResults, QueryParameter... parameters);

	boolean isTransient(Object data, TynamoClassDescriptor classDescriptor);

	@CommitAfter
	<T> T addToCollection(CollectionDescriptor descriptor, T element, Object collectionOwner);

	@CommitAfter
	void removeFromCollection(CollectionDescriptor descriptor, Object element, Object collectionOwner);
}
