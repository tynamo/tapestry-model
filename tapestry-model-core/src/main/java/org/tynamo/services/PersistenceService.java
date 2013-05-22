/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.tynamo.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;

// To be replaced with an internal service. User are discouraged to use this service even if model pages are using it
@Deprecated 
public interface PersistenceService
{

	/**
	 * Returns the persistent instance of the given entity class with the given identifier,
	 * or null if there is no such persistent instance.
	 */
	<T> T getInstance(Class<T> type, Serializable id);

	<T> List<T> getInstances(Class<T> type);

	<T> List<T> getInstances(Class<T> type, int startIndex, int maxResults);

	int count(Class type);

	<T> T save(T instance);

	void remove(Object instance);

	/**
	 * Removes all the elements in the specified collection
	 */
	void removeAll(Collection collection);

	/**
	 * Adds and element to the collectionOwner's collection described by the descriptor
	 */
	<T> T addToCollection(CollectionDescriptor descriptor, T element, Object collectionOwner);

	/**
	 * Removes the element from the collectionOwner's collection described by the descriptor
	 */
	void removeFromCollection(CollectionDescriptor descriptor, Object element, Object collectionOwner);

	List getOrphanInstances(CollectionDescriptor descriptor, Object owner);

	/**
	 * Returns the identifier value of the given entity
	 */
	Serializable getIdentifier(Object data, TynamoClassDescriptor classDescriptor);

	/**
	 * Returns the identifier value of the given entity
	 */
	Serializable getIdentifier(Object data);

	<T> GridDataSource getGridDataSource(Class<T> type);
}
