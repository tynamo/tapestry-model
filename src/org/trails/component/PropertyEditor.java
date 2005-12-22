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
package org.trails.component;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.util.ComponentAddress;
import org.apache.tapestry.valid.BaseValidator;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.NumberValidator;
import org.apache.tapestry.valid.StringValidator;

import org.trails.descriptor.EditorService;
import org.trails.descriptor.IPropertyDescriptor;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class PropertyEditor extends TrailsComponent
{
    public abstract IPropertyDescriptor getDescriptor();

    public abstract void setDescriptor(IPropertyDescriptor Descriptor);
    
    public abstract Object getModel();
    
    @InjectObject("spring:editorService")
    public abstract EditorService getEditorService();
    
    public Block getBlock()
    {
        
        Block editorBlock = (Block)
            getEditorAddress().findComponent(getPage().getRequestCycle());
        editorBlock.getPage().setProperty("model", getModel());
        editorBlock.getPage().setProperty("descriptor", getDescriptor());
        editorBlock.getPage().setProperty("editPageName", getPage().getPageName());
        return editorBlock;
    }

    public ComponentAddress getEditorAddress()
    {
        
        return getEditorService().findEditor(getDescriptor());
    }
}
