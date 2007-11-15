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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.link.ILinkRenderer;
import org.trails.callback.CallbackStack;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.page.PageResolver;
import org.trails.persistence.PersistenceService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * This component produces a editor for a ManyToOne or ManyToMany collection.
 * It allows a user to edit a collection property
 *
 * @author Chris Nelson
 */
public abstract class EditCollection extends TrailsComponent
{

	protected static final Log LOG = LogFactory.getLog(EditCollection.class);

	@Parameter
	public abstract CallbackStack getCallbackStack();

	public abstract void setCallbackStack(CallbackStack stack);

	@Parameter(required = true)
	public abstract Collection getCollection();

	public abstract void setCollection(Collection Collection);

	/**
	 * The object which owns the collection being edited
	 */
	@Parameter(required = false, defaultValue = "page.model")
	public abstract Object getModel();

	public abstract void setModel(Object Model);

	/**
	 * Ognl expression to invoke on the model to create a new child instance
	 */
	@Parameter(required = false)
	public abstract String getCreateExpression();

	public abstract void setCreateExpression(String CreateExpression);

	/**
	 * The CollectionDescriptor for the collection being edited
	 */
	@Parameter(required = true)
	public abstract IPropertyDescriptor getPropertyDescriptor();

	public abstract void setPropertyDescriptor(IPropertyDescriptor PropertyDescriptor);

	public abstract Object getCurrentObject();

	public abstract void setCurrentObject(Object CurrentObject);

	@Parameter(required = false, defaultValue = "page.descriptorService")
	public abstract DescriptorService getDescriptorService();

	@Parameter(required = false, defaultValue = "page.persistenceService")
	public abstract PersistenceService getPersistenceService();

	@Parameter(required = false, defaultValue = "not(collectionDescriptor.childRelationship)")
	public abstract boolean getAddFromExisting();

	@Parameter(required = false, defaultValue = "true")
	public abstract boolean isAllowCreate();

	public abstract int getIndex();

	public abstract void setIndex(int index);

	@Asset("classpath:move_up.gif")
	public abstract IAsset getUpImage();

	@Asset("classpath:move_down.gif")
	public abstract IAsset getDownImage();

	private List selected = new ArrayList();

	/**
	 * 
	 * org.apache.tapestry.contrib.link.ButtonLinkRenderer
	 * @return
	 */
	@InjectObject(value = "service:trails.core.AddNewLinkRenderer")
	public abstract ILinkRenderer getRenderer();


	/**
	 * (non-Javadoc)
	 *
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

	@InjectObject("service:trails.core.PageResolver")
	public abstract PageResolver getPageResolver();

	/**
	 * @param cycle
	 */
	public void remove(IRequestCycle cycle)
	{
		int i = 0;
		// TODO CN - This code stinks (I wrote it).  Isn't there a better way??
		ArrayList deleting = new ArrayList();
		for (Iterator iter = getCollection().iterator(); iter.hasNext();)
		{

			Object element = (Object) iter.next();

			if (((Boolean) getSelected().get(i)).booleanValue())
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
		} else
		{
			// but do here
			return new IdentifierSelectionModel(getPersistenceService().getAllInstances(getCollectionDescriptor().getElementType()),
				elementDescriptor.getIdentifierDescriptor().getName());
		}
	}

	/**
	 * @param cycle
	 */
	public void moveUp(IRequestCycle cycle)
	{
		List list = (List) getCollection();
		for (int i = 1; i < getSelected().size(); i++)
		{
			if (((Boolean) getSelected().get(i)).booleanValue())
			{
				swap(list, i, i - 1);
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
		List list = (List) getCollection();
		for (int i = 0; i < getSelected().size() - 1; i++)
		{
			if (((Boolean) getSelected().get(i)).booleanValue())
			{
				swap(list, i, i + 1);
			}
		}
	}
}
