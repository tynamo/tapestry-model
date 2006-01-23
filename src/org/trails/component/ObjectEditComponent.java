/*
 * Created on Dec 11, 2004
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
package org.trails.component;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.components.Block;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.page.EditPage;

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class ObjectEditComponent extends ObjectComponent
{

    public boolean hasBlock(String propertyName)
    {
        if (getPage().getComponents().containsKey(propertyName))
        {
            return true;
        }
//        else {
//            IPage editPage = getEditPage();
//            if (editPage != null)
//            {
//                return editPage.getComponents().containsKey(propertyName);
//            }
//        }
        return false;
    }

    protected EditPage getEditPage()
    {
        EditPage editPage = (EditPage)Utils.findPage(getPage().getRequestCycle(), 
            Utils.unqualify(getModel().getClass().getName()) + "Edit",
            "Edit");
        return editPage;
    }

    public Block getBlock(String propertyName)
    {
        if (getPage().getComponents().containsKey(propertyName))
        {
            return (Block)getPage().getComponent(propertyName);
        }
//        EditPage editPage = getEditPage();
//        if (editPage != null)
//        {
//            
//            if (editPage.getComponents().containsKey(propertyName))
//            {
//                editPage.setModel(getModel());
//                return (Block)editPage.getComponent(propertyName);
//            }
//        }
        return null;
    }

    public abstract Object getModel();

    public abstract void setModel(Object model);

}
