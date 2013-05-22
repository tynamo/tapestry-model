/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.tynamo.model.jpa.services;

import java.util.List;

import org.apache.tapestry5.jpa.annotations.CommitAfter;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.PersistenceService;

public interface JpaPersistenceService extends PersistenceService
{

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

	@CommitAfter
	<T> T addToCollection(CollectionDescriptor descriptor, T element, Object collectionOwner);

	@CommitAfter
	void removeFromCollection(CollectionDescriptor descriptor, Object element, Object collectionOwner);

}