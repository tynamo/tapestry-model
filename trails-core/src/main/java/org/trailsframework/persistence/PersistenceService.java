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
package org.trails.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.trails.descriptor.IClassDescriptor;

public interface PersistenceService
{

	public <T> T getInstance(Class<T> type, Serializable id);

	<T> T loadInstance(final Class<T> type, Serializable id);

	public <T> List<T> getAllInstances(Class<T> type);

	public <T> List<T> getInstances(Class<T> type, int startIndex, int maxResults);

	/**
	 * @return a List containing all the classes this persistence
	 *         service knows about
	 */
	public List getAllTypes();

	public <T> T save(T instance);

	public void remove(Object instance);
	public void removeAll(Collection collection);

	/**
	 * A convenience method for getting a singleton instance of specific type
	 *
	 * @param <T>  Specific type of the entity
	 * @param type Type of singleton entity you want return
	 * @return Returns the singleton entity of requested type
	 */
	public <T> T getInstance(final Class<T> type);

	Serializable getIdentifier(Object data, IClassDescriptor classDescriptor);

	boolean isTransient(Object data, IClassDescriptor classDescriptor);

	<T> T saveCollectionElement(String addExpression, T member, Object parent);

	void removeCollectionElement(String removeExpression, Object member, Object parent);

}
