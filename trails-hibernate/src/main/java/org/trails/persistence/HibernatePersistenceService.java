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

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;


public interface HibernatePersistenceService extends PersistenceService
{

	public <T> T getInstance(DetachedCriteria criteria);

	public List getInstances(DetachedCriteria criteria);

	public List getInstances(DetachedCriteria criteria, int startIndex, int maxResults);

	public int count(DetachedCriteria criteria);

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
	public List getInstances(Object example);

	public <T> T reload(T instance);

	public <T> T merge(T instance);

	public <T> T saveOrUpdate(T instance);

}
