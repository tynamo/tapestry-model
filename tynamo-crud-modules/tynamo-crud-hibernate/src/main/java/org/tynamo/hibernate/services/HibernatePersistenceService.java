/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.tynamo.hibernate.services;

import org.hibernate.criterion.DetachedCriteria;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.services.PersistenceService;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import java.util.List;

public interface HibernatePersistenceService extends PersistenceService
{

	@CommitAfter
	<T> T save(T instance);

	@CommitAfter
	void remove(Object instance);

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
	public <T> List<T> getInstances(T example, TrailsClassDescriptor classDescriptor);

	public <T> T merge(T instance);

	public <T> T saveOrUpdate(T instance);

	public List findByQuery(String queryString);

	public List findByQuery(String queryString, QueryParameter... parameters);

	public List findByQuery(String queryString, int startIndex, int maxResults, QueryParameter... parameters);
}
