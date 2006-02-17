/*
 * Created on Feb 27, 2005
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.trails.page.EditPage;

/**
 * @author Chris Nelson
 *
 *  Returns control to an EditPage
 */
public class EditCallback extends TrailsCallback
{
    protected Object model;
    
    private boolean modelNew;
    
    /**
     * 
     */
    public EditCallback(String pageName, Object model)
    {
        super(pageName);
        this.model = model;
    }
    
    public EditCallback(String pageName, Object model, boolean modelNew)
    {
    	this(pageName, model);
    	this.modelNew = modelNew;
    }
    
    /* (non-Javadoc)
     * @see org.apache.tapestry.callback.ICallback#performCallback(org.apache.tapestry.IRequestCycle)
     */
    public void performCallback(IRequestCycle cycle)
    {
        EditPage editPage = (EditPage)cycle.getPage(getPageName());
        editPage.setModel(model);
        cycle.activate(editPage);
    }

	public Object getModel()
	{
		return model;
	}

	public void setModel(Object model)
	{
		this.model = model;
	}

	public boolean isModelNew()
	{
		return modelNew;
	}

	public void setModelNew(boolean modelNew)
	{
		this.modelNew = modelNew;
	}

	/**
	 * We should always replace this callback if its model is new. 
	 * This works to make sure that after a model is saved for the first
	 * time its call back is replaced and we can go back to the right one.
	 */
	@Override
	public boolean shouldReplace(ICallback callback)
	{
		if (callback instanceof EditCallback)
		{
			EditCallback editCallback = (EditCallback)callback;
			if (editCallback.isModelNew())
			{
				return true;
			}
			else
			{
				return this.equals(editCallback);
			}
		}
		else
		{
			return false;
		}
	}
	
	

}
