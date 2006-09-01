/*
 * Created on Feb 28, 2005
 *
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
package org.trails.callback;

import java.util.HashMap;

import ognl.Ognl;
import ognl.OgnlException;

import org.trails.TrailsRuntimeException;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.persistence.PersistenceService;

/**
 * This guy is responsible for returning from an add or remove on a
 * collection.
 */
public class CollectionCallback extends EditCallback
{
    private CollectionDescriptor collectionDescriptor;
	
	private boolean childRelationship;
    
    /**
     * @param pageName
     * @param model
     */
    public CollectionCallback(String pageName, Object model, CollectionDescriptor collectionDescriptor)
    {
        super(pageName, model);
        this.collectionDescriptor = collectionDescriptor;
    }

    public void save(PersistenceService persistenceService, Object newObject)
    {
        executeOgnlExpression(collectionDescriptor.findAddExpression(), newObject);
        persistenceService.save(getModel());
    }
    
    public void remove(PersistenceService persistenceService, Object object)
    {
        executeOgnlExpression(collectionDescriptor.findRemoveExpression(), object);
        persistenceService.save(getModel());
    }
    
    /**
     * @param previousModel
     */
    private void executeOgnlExpression(String ognlExpression, Object newObject)
    {
        HashMap context = new HashMap();
        context.put("member", newObject);

        try
        {
            Ognl.getValue(ognlExpression + "(#member)", context, model);
        }catch (OgnlException e)
        {
            throw new TrailsRuntimeException(e);
        }
    }
    
    public boolean isChildRelationship()
    {
        return childRelationship;
    }
    

    public void setChildRelationship(boolean child)
    {
        this.childRelationship = child;
    }
	
}
