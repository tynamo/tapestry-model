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
package org.trails.page;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.Lifecycle;
import org.apache.tapestry.callback.ICallback;
import org.hibernate.validator.InvalidStateException;
import org.trails.callback.CollectionCallback;
import org.trails.callback.EditCallback;
import org.trails.persistence.PersistenceException;
import org.trails.validation.TrailsValidationDelegate;


/**
 * @author Chris Nelson
 *
 * This page will edit an instance contained in the model property
 */
public abstract class EditPage extends ModelPage
{
    @Bean(lifecycle=Lifecycle.REQUEST)
    public abstract TrailsValidationDelegate getDelegate();

    /**
     * This property allows components to change the page during 
     * the middle of the rewind without causing a StaleLinkException
     * @return
     */
    public abstract ICallback getNextPage();

    public abstract void setNextPage(ICallback NextPage);

    public void save(IRequestCycle cycle)
    {
        save();
        
    }

    
    /* (non-Javadoc)
     * @see org.trails.page.TrailsPage#pushCallback()
     */
    public void pushCallback()
    {
        getCallbackStack().push(new EditCallback(getPageName(), getModel()));
    }
    
    protected boolean save()
    {
        if (!getDelegate().getHasErrors())
        {
            // We get hibernate errors when the cascade tries to
            // put the same object on the session twice
//            if (!cameFromChildCollection())
//            {
                try
                {
                    setModel(getPersistenceService().save(getModel()));
                }
                catch (PersistenceException pe)
                {
                    getDelegate().record(pe);
                    return false;
                }
                catch (InvalidStateException ivex)
                {
                    getDelegate().record(getClassDescriptor(), ivex);
                    return false;
                }
//            }
            return true;
        }
        return false;
    }

    public void remove(IRequestCycle cycle)
    {
//        if (!cameFromChildCollection())
//        {
            getPersistenceService().remove(getModel());
//        }
        ICallback callback = (ICallback)getCallbackStack().pop();
        if (callback instanceof CollectionCallback)
        {
            ((CollectionCallback)callback).remove(getModel());
        }
        callback.performCallback(cycle);
    }

    /*
     * This is here so that if a component needs to go to a new page it 
     * won't do so in the middle of a rewind and generate a StaleLink
     */
    public void onFormSubmit(IRequestCycle cycle)
    {
        if (getNextPage() != null)
        {
            getNextPage().performCallback(cycle);
        }
    }
    
    /**
     * @param cycle
     */
    public void saveAndReturn(IRequestCycle cycle)
    {
        if (save())
        {
            
            ICallback callback = (ICallback)getCallbackStack().pop();
            if (callback instanceof CollectionCallback)
            {
                ((CollectionCallback)callback).add(getModel());
            }
            callback.performCallback(cycle);
        }
    }

    /**
     * @return
     */
    public String getTitle()
    {
    	Object[] params = new Object[]{getClassDescriptor().getDisplayName()};
        if (cameFromCollection() && isModelNew())
        {
            return getResourceBundleMessageSource().getMessageWithDefaultValue("org.trails.i18n.add",params, getLocale(), "[TRAILS][ORG.TRAILS.I18N.ADD]");
        }
        else
        {
            return getResourceBundleMessageSource().getMessageWithDefaultValue("org.trails.i18n.edit",params, getLocale(), "[TRAILS][ORG.TRAILS.I18N.EDIT]");
        }
    }

    public boolean cameFromCollection()
    {
        
        return getCallbackStack().peek() instanceof CollectionCallback;
    }
    
    public boolean cameFromChildCollection()
    {
        
        if (getCallbackStack().peek() instanceof CollectionCallback)
        {
            return ((CollectionCallback)getCallbackStack().peek()).isChildRelationship();
        }
        else return false;
    }
}
