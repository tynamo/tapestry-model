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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.trails.page.ListPage;

/**
 * @author Chris Nelson
 * 
 * Returns control to a ListPage
 */
public class ListCallback implements ICallback
{
    private String pageName;
    
    private String typeName;
    
    public ListCallback(String pageName, String typeName)
    {
        this.pageName = pageName;
        this.typeName = typeName;
    }
    
    /* (non-Javadoc)
     * @see org.apache.tapestry.callback.ICallback#performCallback(org.apache.tapestry.IRequestCycle)
     */
    public void performCallback(IRequestCycle cycle)
    {
        ListPage listPage = (ListPage)cycle.getPage(pageName);
        listPage.setTypeName(typeName);
        listPage.loadInstances();
        cycle.activate(listPage);
    }

    /**
     * @return Returns the pageName.
     */
    public String getPageName()
    {
        return pageName;
    }
    /**
     * @param pageName The pageName to set.
     */
    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }
    /**
     * @return Returns the typeName.
     */
    public String getTypeName()
    {
        return typeName;
    }
    /**
     * @param typeName The typeName to set.
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
}
