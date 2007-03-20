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
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface PersistenceService
{
    public <T> T getInstance(Class<T> type, Serializable id);
    
    public <T> List<T> getAllInstances(Class<T> type);
    
    /**
     * 
     * @return a List containing all the classes this persistence 
     * service knows about
     */
    public List getAllTypes();

    public <T> T save(T instance);

    public void remove(Object instance);

    /**
     * A convenience method for getting a singleton instance of specific type
     * @param <T> Specific type of the entity
     * @param type Type of singleton entity you want return 
     * @return Returns the singleton entity of requested type
     */
    public <T> T getInstance( final Class<T> type);

    public <T> T getInstance(DetachedCriteria criteria);

    public List getInstances(DetachedCriteria criteria);
    
    public List getInstances(DetachedCriteria criteria, int startIndex, int maxResults);
    
    public int count(DetachedCriteria criteria);
    
    /**
     * 
     * @param model to attach to the current persistence session
     */
    public void reattach(Object model);

    /**
     * Does a query by example
     * @param example
     * @return
     */
	public List getInstances(Object example);

    public <T> T reload(T instance);

    public <T> T merge(T instance);

    public <T> T saveOrUpdate(T instance);
    
}
