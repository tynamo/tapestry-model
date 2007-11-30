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

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.*;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.link.ILinkRenderer;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.page.PageResolver;
import org.trails.persistence.PersistenceService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * This component produces a editor for a ManyToOne or ManyToMany collection. It allows a user to edit a collection
 * property
 *
 * @author Chris Nelson
 */
@ComponentClass(allowBody = false)
public abstract class EditCollection extends TrailsComponent
{

	protected static final Log LOG = LogFactory.getLog(EditCollection.class);

	@Parameter(required = true)
	public abstract Collection getCollection();

	/**
	 * The object which owns the collection being edited
	 */
	@Parameter(required = false, defaultValue = "page.model")
	public abstract Object getModel();

	/**
	 * Ognl expression to invoke on the model to create a new child instance
	 */
	@Parameter(required = false)
	public abstract String getCreateExpression();

	/**
	 * @return The CollectionDescriptor for the collection being edited
	 */
	@Parameter(required = true)
	public abstract CollectionDescriptor getCollectionDescriptor();

	public abstract Object getCurrentObject();

	public abstract void setCurrentObject(Object CurrentObject);

	@Parameter(defaultValue = "page.descriptorService")
	public abstract DescriptorService getDescriptorService();

	@Parameter(defaultValue = "page.persistenceService")
	public abstract PersistenceService getPersistenceService();

	@Parameter(defaultValue = "not(collectionDescriptor.childRelationship)")
	public abstract boolean getAddFromExisting();

	@Parameter(defaultValue = "true")
	public abstract boolean isAllowCreate();

	@Parameter(defaultValue = "collectionDescriptor.allowRemove")
	public abstract boolean isAllowRemove();

	public abstract int getIndex();

	public abstract void setIndex(int index);

	@Asset("classpath:move_up.gif")
	public abstract IAsset getUpImage();

	@Asset("classpath:move_down.gif")
	public abstract IAsset getDownImage();

	private List<Boolean> selected = new ArrayList<Boolean>();

	/**
	 * org.apache.tapestry.contrib.link.ButtonLinkRenderer
	 *
	 * @return
	 */
	@InjectObject(value = "service:trails.core.AddNewLinkRenderer")
	public abstract ILinkRenderer getRenderer();

	/**
	 * (non-Javadoc)
	 *
	 * @see org.apache.tapestry.AbstractComponent#prepareForRender(org.apache.tapestry.IRequestCycle)
	 */
	protected void prepareForRender(IRequestCycle cycle)
	{
		super.prepareForRender(cycle);
		selected = buildSelectedList();
	}

	public List<Boolean> buildSelectedList()
	{
		ArrayList<Boolean> selected = new ArrayList<Boolean>();
		if (getCollection() != null)
		{
			selected = new ArrayList<Boolean>(getCollection().size());
			for (Object o : getCollection())
			{
				selected.add(false);
			}
		}
		return selected;
	}

	@InjectObject("service:trails.core.PageResolver")
	public abstract PageResolver getPageResolver();

	public void remove()
	{
		int i = 0;
		// TODO CN - This code stinks (I wrote it).  Isn't there a better way??
		ArrayList deleting = new ArrayList();
		for (Object element : getCollection())
		{
			if (getSelected().get(i))
			{
				deleting.add(element);
			}
			i++;
		}

		for (Object element : deleting)
		{
			Utils.executeOgnlExpression(getCollectionDescriptor().getRemoveExpression(), element, getModel());
		}
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
	public List<Boolean> getSelected()
	{
		return selected;
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
		IClassDescriptor elementDescriptor =
				getDescriptorService().getClassDescriptor(getCollectionDescriptor().getElementType());
		// don't allow use to select from all here
		if (getCollectionDescriptor().isChildRelationship())
		{
			return new IdentifierSelectionModel(getSelectedList(),
					elementDescriptor.getIdentifierDescriptor().getName());
		} else
		{
			// but do here
			return new IdentifierSelectionModel(
					getPersistenceService().getAllInstances(getCollectionDescriptor().getElementType()),
					elementDescriptor.getIdentifierDescriptor().getName());
		}
	}

	public void moveUp()
	{
		List list = (List) getCollection();
		for (int i = 1; i < getSelected().size(); i++)
		{
			if (getSelected().get(i))
			{
				if (getCollectionDescriptor().getSwapExpression() == null)
				{
					Collections.swap(list, i, i - 1);
				} else
				{
					try
					{
						Ognl.getValue(getCollectionDescriptor().getSwapExpression() + "(" + i + "," + (i - 1) + ")",
								getModel());
					} catch (OgnlException e)
					{
						LOG.error(e.getMessage());
					}
				}
			}
		}
	}

	public void moveDown()
	{
		List list = (List) getCollection();
		for (int i = 0; i < getSelected().size() - 1; i++)
		{
			if (getCollectionDescriptor().getSwapExpression() == null)
			{
				Collections.swap(list, i, i + 1);
			} else
			{
				try
				{
					Ognl.getValue(getCollectionDescriptor().getSwapExpression() + "(" + i + "," + (i + 1) + ")",
							getModel());
				} catch (OgnlException e)
				{
					LOG.error(e.getMessage());
				}
			}
		}
	}
}
