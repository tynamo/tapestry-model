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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.trails.page.EditPage;

/**
 * @author Chris Nelson
 *
 *  Returns control to an EditPage
 */
public class EditCallback implements ICallback
{
    protected String pageName;
    protected Object model;
    
    /**
     * 
     */
    public EditCallback(String pageName, Object model)
    {
        this.pageName = pageName;
        this.model = model;
    }
    
    /* (non-Javadoc)
     * @see org.apache.tapestry.callback.ICallback#performCallback(org.apache.tapestry.IRequestCycle)
     */
    public void performCallback(IRequestCycle cycle)
    {
        EditPage editPage = (EditPage)cycle.getPage(pageName);
        editPage.setModel(model);
        cycle.activate(editPage);
    }

    public String getPageName()
    {
        return pageName;
    }

}
