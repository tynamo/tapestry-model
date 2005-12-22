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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.trails.TrailsRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.callback.CollectionCallback;
import org.trails.callback.EditCallback;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.page.EditPage;
import org.trails.page.EditorBlockPage;
import org.trails.persistence.PersistenceService;


/**
 * @author Chris Nelson
 * 
 * This component produces a editor for a ManyToOne or ManyToMany collection
 */
public abstract class EditCollection extends TrailsComponent 
{
    @InjectState("callbackStack")
    public abstract Stack getCallbackStack();
    
    public abstract void setCallbackStack(Stack stack);
    
    public abstract Collection getCollection();

    public abstract void setCollection(Collection Collection);
    
    public abstract Object getModel();
    
    public abstract void setModel(Object Model);

    public abstract IPropertyDescriptor getPropertyDescriptor();

    public abstract void setPropertyDescriptor(
        IPropertyDescriptor PropertyDescriptor);

    public abstract Object getCurrentObject();

    public abstract void setCurrentObject(Object CurrentObject);
    
    public abstract DescriptorService getDescriptorService();
    
    public abstract PersistenceService getPersistenceService();

    private List selected = new ArrayList();

    
    /* (non-Javadoc)
     * @see org.apache.tapestry.AbstractComponent#prepareForRender(org.apache.tapestry.IRequestCycle)
     */
    protected void prepareForRender(IRequestCycle arg0)
    {
        // TODO Auto-generated method stub
        super.prepareForRender(arg0);
        buildSelectedList();
    }
    
    void buildSelectedList()
    {
        if (getCollection() != null)
        {
	        selected = new ArrayList();
	        for (Iterator iter = getCollection().iterator(); iter.hasNext();)
	        {
	            iter.next();
	            selected.add(new Boolean(false));
	        }
        }
    }

    public CollectionDescriptor getCollectionDescriptor()
    {
        return (CollectionDescriptor) getPropertyDescriptor();
    }

    public void showAddPage(IRequestCycle cycle)
    {
        getCallbackStack().push(buildCallback());

        EditPage editPage = (EditPage) Utils.findPage(cycle,
                Utils.unqualify(getCollectionDescriptor().getElementType()
                                    .getName() + "Edit"), "Edit");
        try
        {
            // we need to do some indirection to avoid a StaleLink
            EditCallback nextPage = new EditCallback(editPage.getPageName(),
                getCollectionDescriptor().getElementType().newInstance());
            String currentEditPageName = ((EditorBlockPage)getPage()).getEditPageName();
            ((EditPage)cycle.getPage(currentEditPageName)).setNextPage(nextPage);
            //editPage.setModel(getCollectionDescriptor().getElementType().newInstance());
            
        }catch (Exception ex)
        {
            throw new TrailsRuntimeException(ex);
        }
    }

    CollectionCallback buildCallback()
    {
        CollectionCallback callback = new CollectionCallback(
            ((EditorBlockPage)getPage()).getEditPageName(), getModel(), findExpression("add"), findExpression("remove"));
        callback.setChildRelationship(getCollectionDescriptor().isChildRelationship());
        return callback;
    }

    /**
     * @param method the method to look for, usually add or remove
     * @return the ogln expression to use to add or remove a member to the
     * collection.  Will look for a addName method where Name is
     * the unqualified element class name, if there isn't one it will use
     * the collection's add method.
     */
    String findExpression(String method)
    {
        Method addMethod = null;

        try
        {
            addMethod = getModel().getClass().getMethod("add" +
                    Utils.unqualify(getCollectionDescriptor().getElementType()
                                        .getName()),
                    new Class[] { getCollectionDescriptor().getElementType() });
        }catch (NoSuchMethodException ex)
        {
            // if we don't have one...
            return getCollectionDescriptor().getName() + ".add";
        }catch (Exception e)
        {
            throw new TrailsRuntimeException(e);
        }

        return addMethod.getName();
    }

    /**
     * @param cycleMock
     */
    public void remove(IRequestCycle cycle)
    {
        int i=0;
        // TODO CN - This code stinks (I wrote it).  Isn't there a better way??
        ArrayList deleting = new ArrayList();
        for (Iterator iter = getCollection().iterator(); iter.hasNext();)
        {
            
            Object element = (Object) iter.next();

            if (((Boolean)getSelected().get(i)).booleanValue())
            {
                deleting.add(element);
            }
            i++;
        }
        getCollection().removeAll(deleting);
    }
    
    public List getSelectedList()
    {
        ArrayList selectedList = new ArrayList();
        selectedList.addAll(getCollection());
        return selectedList;
    }
    
    public void setSelectedList(List selected)
    {
        if (selected != null)
        {
	        getCollection().clear();
	        getCollection().addAll(selected);
        }
    }
    
    /**
     * @return Returns the toBeDeleted.
     */
    public List getSelected()
    {
        return selected;
    }
    
    /**
     * @param toBeDeleted The toBeDeleted to set.
     */
    public void setSelected(List toBeDeleted)
    {
        this.selected = toBeDeleted;
    }
    
   
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        // TODO Auto-generated method stub
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * @return
     */
    public String getSortMode()
    {
        return isList() ? SortMode.USER : SortMode.LABEL;
    }

    public boolean isList()
    {
        return getCollection() instanceof List;
    }

    /**
     * @return
     */
    public IPropertySelectionModel getSelectionModel()
    {
        IdentifierSelectionModel selectionModel = null;
        IClassDescriptor elementDescriptor = getDescriptorService().getClassDescriptor(getCollectionDescriptor().getElementType());
        // don't allow use to select from all here
        if (getCollectionDescriptor().isChildRelationship())
        {
            return new IdentifierSelectionModel(getSelectedList(), 
                elementDescriptor.getIdentifierDescriptor().getName());
        }
        // but do here
        else
        {
            return new IdentifierSelectionModel(getPersistenceService().getAllInstances(getCollectionDescriptor().getElementType()), 
                elementDescriptor.getIdentifierDescriptor().getName());
        }
    }

    /**
     * @param cycle
     */
    public void moveUp(IRequestCycle cycle)
    {
        List list = (List)getCollection();
        for (int i=1; i < getSelected().size(); i++)
        {
            if(((Boolean)getSelected().get(i)).booleanValue())
            {
                swap(list, i, i-1);
            }
        }
    }

    private void swap(List list, int fromIndex, int toIndex)
    {
        Object from = list.get(fromIndex);
        Object to = list.get(toIndex);
        list.set(fromIndex, to);
        list.set(toIndex, from);
    }

    /**
     * @param cycle
     */
    public void moveDown(IRequestCycle cycle)
    {
        List list = (List)getCollection();
        for (int i=0; i < getSelected().size() -1; i++)
        {
            if(((Boolean)getSelected().get(i)).booleanValue())
            {
                swap(list, i, i+1);
            }
        } 
    }
}
