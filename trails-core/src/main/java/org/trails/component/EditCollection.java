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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.TrailsRuntimeException;
import org.trails.callback.CallbackStack;
import org.trails.callback.CollectionCallback;
import org.trails.callback.EditCallback;
import org.trails.callback.EditCollectionMemberCallback;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.page.EditPage;
import org.trails.page.PageResolver;
import org.trails.page.TrailsPage.PageType;
import org.trails.persistence.PersistenceService;


/**
 * @author Chris Nelson
 * 
 * This component produces a editor for a ManyToOne or ManyToMany collection
 */
public abstract class EditCollection extends TrailsComponent 
{

    protected static final Log LOG = LogFactory.getLog(EditCollection.class);
	
    @InjectState("callbackStack")
    public abstract CallbackStack getCallbackStack();
    
    public abstract void setCallbackStack(CallbackStack stack);
    
    public abstract Collection getCollection();

    public abstract void setCollection(Collection Collection);
    
    public abstract Object getModel();
    
    public abstract void setModel(Object Model);
    
    public abstract String getCreateExpression();

	public abstract void setCreateExpression(String CreateExpression);

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

	@InjectObject("spring:pageResolver")
	public abstract PageResolver getPageResolver();
	
    public IPage edit(Object member)
    {
        EditCollectionMemberCallback callback = new EditCollectionMemberCallback(
            getPage().getRequestCycle().getPage().getPageName(),
            getModel(),
            getCollectionDescriptor());
        getCallbackStack().push(callback);
        EditPage editPage = (EditPage)getPageResolver().resolvePage(
                getPage().getRequestCycle(),
                Utils.checkForCGLIB(member.getClass()).getName(),
                PageType.EDIT);
        editPage.setModel(member);
        return editPage;
    }
    
    
    public void showAddPage(IRequestCycle cycle)
    {
        getCallbackStack().push(buildCallback());

        EditPage editPage = (EditPage)getPageResolver().resolvePage(cycle,
        		getCollectionDescriptor().getElementType().getName(),
        		PageType.EDIT);
        try
        {
            // we need to do some indirection to avoid a StaleLink
            EditCallback nextPage = new EditCallback(editPage.getPageName(),
                buildNewMemberInstance());
            String currentEditPageName = getPage().getRequestCycle().getPage().getPageName();
            ((EditPage)cycle.getPage(currentEditPageName)).setNextPage(nextPage);
            //editPage.setModel(getCollectionDescriptor().getElementType().newInstance());
            
        }catch (Exception ex)
        {
            throw new TrailsRuntimeException(ex);
        }
    }

	protected Object buildNewMemberInstance() throws InstantiationException, IllegalAccessException
	{
		Object object;
		if (getCreateExpression() == null)
		{
			object =  getCollectionDescriptor().getElementType().newInstance();
		}
		else
		{
			try
			{
				object = Ognl.getValue(getCreateExpression(), getModel());
			}
			catch (OgnlException oe)
			{
				oe.printStackTrace();
				return null;
			}
		}
        
        if (getCollectionDescriptor().getInverseProperty() != null && getCollectionDescriptor().isOneToMany())
		{
			try 
			{
				Ognl.setValue(getCollectionDescriptor().getInverseProperty(), object, getModel());
			} catch (OgnlException e) {
				LOG.error(e.getMessage());
			}
		}
        
        return object;
	}
	
    CollectionCallback buildCallback()
    {
        CollectionCallback callback = new CollectionCallback(
            getPage().getRequestCycle().getPage().getPageName(), getModel(), getCollectionDescriptor());
        callback.setChildRelationship(getCollectionDescriptor().isChildRelationship());
        return callback;
    }
    
    /**
     * @param cycle
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
        IClassDescriptor elementDescriptor = getDescriptorService().getClassDescriptor(getCollectionDescriptor().getElementType());

        // don't allow use to select from all here
        if (getCollectionDescriptor().isChildRelationship())
        {
            return new IdentifierSelectionModel(getSelectedList(), elementDescriptor.getIdentifierDescriptor().getName());
        }
        // but do here
        else if (getCollectionDescriptor().getInverseProperty() != null && getCollectionDescriptor().isOneToMany())
        {
            DetachedCriteria criteria = DetachedCriteria.forClass(getCollectionDescriptor().getElementType());
            String identifier = elementDescriptor.getIdentifierDescriptor().getName();
            if (getModel() != null)
            {
                try
                {
                    criteria.add(
                            Restrictions.disjunction()
                                    .add(Restrictions.isNull(getCollectionDescriptor().getInverseProperty()))
                                    .add(Restrictions.eq(getCollectionDescriptor().getInverseProperty() + "." + identifier, Ognl.getValue(elementDescriptor.getIdentifierDescriptor().getName(), getModel()))));
                } catch (OgnlException e)
                {
                    LOG.error(e.getMessage());
                }
            } else
            {
                criteria.add(Restrictions.isNull(getCollectionDescriptor().getInverseProperty()));
            }

            return new IdentifierSelectionModel(getPersistenceService().getInstances(criteria), elementDescriptor.getIdentifierDescriptor().getName());
        } else
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
