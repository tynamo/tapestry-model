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

/**
 * This guy is responsible for returning from an add or remove on a
 * collection.
 */
public class CollectionCallback extends EditCallback
{
    private String addOgnlExpression;
    
    private String removeOgnlExpression;
	
	private boolean childRelationship;
    
    /**
     * @param pageName
     * @param model
     */
    public CollectionCallback(String pageName, Object model, String addOgnl, String removeOgnl)
    {
        super(pageName, model);
        this.addOgnlExpression = addOgnl;
        this.removeOgnlExpression = removeOgnl;
    }

    public void add(Object newObject)
    {
        executeOgnlExpression(addOgnlExpression, newObject);
    }
    
    public void remove(Object object)
    {
        executeOgnlExpression(removeOgnlExpression, object);
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
    
    /**
     * @return Returns the addOgnlExpression.
     */
    public String getAddOgnlExpression()
    {
        return addOgnlExpression;
    }
    /**
     * @return Returns the removeOgnlExpression.
     */
    public String getRemoveOgnlExpression()
    {
        return removeOgnlExpression;
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
