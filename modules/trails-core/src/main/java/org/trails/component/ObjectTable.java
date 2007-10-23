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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.contrib.table.components.Table;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.util.ComponentAddress;
import org.trails.descriptor.BlobDescriptorExtension;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceService;

/**
 * Produces a table for the list of instances
 */
@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class ObjectTable extends ClassDescriptorComponent
{
	private static final Log LOG = LogFactory.getLog(ObjectTable.class);

	public static final String LINK_COLUMN = "linkColumnValue";

	public static final String BLOB_COLUMN = "blobColumnValue";

	protected List <TrailsTableColumn> columns = new ArrayList<TrailsTableColumn>();

	@Parameter(required = false, defaultValue = "false", cache = true)
	public abstract boolean isShowCollections();

	public abstract void setShowCollections(boolean ShowCollections);

	@InjectObject("service:trails.core.PersistenceService")
	public abstract PersistenceService getPersistenceService();

	@Parameter
	public abstract List getInstances();

	public abstract void setInstances(List instances);

	public abstract Object getCurrentObject();

	public abstract void setCurrentObject(Object currentObject);

	@Parameter(cache = false, defaultValue = "currentObject")
	public abstract Object getObject();

	public abstract void setObject(Object object);


	@Parameter(cache = false)
	public abstract ITableColumn getColumn();

	public abstract void setColumn(ITableColumn column);

	@Parameter(cache = false)
	public abstract String getRowsClass();

	public abstract void setRowsClass(String rowsClass);

	@Parameter(cache = false)
	public abstract String getColumnsClass();

	public abstract void setColumnsClass(String columnsClass);

	@Parameter(cache = false, defaultValue = "10")
	public abstract int getPageSize();

	public abstract void setPageSize(int pageSize);

	@Parameter
	public abstract int getIndex();

	public abstract void setIndex(int index);

	@Parameter
	public abstract String getInitialSortColumn();

	public abstract void setInitialSortColumn(String initialSortColumn);

	@Parameter(defaultValue = "false")
	public abstract boolean getInitialSortOrder();

	public abstract void setInitialSortOrder(boolean initialSortOrder);

	@Parameter(defaultValue = "literal:session")
	public abstract String getPersist();

	public abstract void setPersist(String persist);


	public ComponentAddress getLinkBlockAddress(IPropertyDescriptor descriptor)
	{
		if (getBlockAddress(descriptor) != null)
		{
			return getBlockAddress(descriptor);
		} else
		{
			return new ComponentAddress(getComponent(LINK_COLUMN));
		}
	}

	@InjectObject("service:tapestry.ognl.ExpressionEvaluator")
	public abstract ExpressionEvaluator getEvaluator();

	@Override
	protected void prepareForRender(IRequestCycle cycle)
	{
		createColumns();
		super.prepareForRender(cycle);
	}

	protected void createColumns()
	{
		LOG.debug("Creating Columns");
		columns = new ArrayList<TrailsTableColumn>();
		for (IPropertyDescriptor descriptor : getPropertyDescriptors())
		{
			if (displaying(descriptor))
			{
				if (getLinkProperty().equals(descriptor.getName())
					&& (getClassDescriptor().isAllowSave() || getClassDescriptor().isAllowRemove()))
				{
					/**
					 * Add a link for the edit page following these rules: a)
					 * Use the identifier descriptor if is displayable (
					 * summary=true ). b) Use the first displayable property if
					 * the identifier property is not displayable
					 * (summary=false)
					 */
					columns.add(new TrailsTableColumn(descriptor, getEvaluator(), getLinkBlockAddress(descriptor)));

				} else if (descriptor.supportsExtension(BlobDescriptorExtension.class.getName())
					&& getBlockAddress(descriptor) == null)
				{
					columns.add(new TrailsTableColumn(descriptor, getEvaluator(), new ComponentAddress(
						getComponent(BLOB_COLUMN))));
				} else
				{
					columns.add(new TrailsTableColumn(descriptor, getEvaluator(), getBlockAddress(descriptor)));
				}
			}
		}
	}

	/**
	 * @param descriptor
	 * @return
	 */
	private boolean displaying(IPropertyDescriptor descriptor)
	{
		if (descriptor.isHidden() || !descriptor.isSummary())
		{
			return false;
		} else if ((descriptor.isCollection() && isShowCollections()) || (!descriptor.isCollection()))
		{
			return true;
		} else
		{
			return false;
		}

	}

	/**
	 * @return
	 */
	public String getIdentifierProperty()
	{
		return this.getIdentifierPropertyDescriptor().getName();

	}

	/**
	 * Returns the name of the property to be used as link to the editor. If the
	 * default Id property is not displayable then return the name of the first
	 * displayable property.
	 * 
	 * @return
	 */
	public String getLinkProperty()
	{
		IPropertyDescriptor propertyDescriptor = this.getIdentifierPropertyDescriptor();
		if (!this.displaying(propertyDescriptor))
			propertyDescriptor = this.getFirstDisplayableProperty();

		return propertyDescriptor.getName();
	}

	/**
	 * Returns the first displayable property.
	 * 
	 * @return
	 */
	protected IPropertyDescriptor getFirstDisplayableProperty()
	{
		for (IPropertyDescriptor descriptor : getPropertyDescriptors())
		{
			if (displaying(descriptor))
			{
				return descriptor;

			}
		}

		return null; // If we get here, that means we have no displayable
		// descriptors
		// TODO check if we should throw an exception instead
	}

	/**
	 * @return
	 */
	protected IPropertyDescriptor getIdentifierPropertyDescriptor()
	{
		return (IPropertyDescriptor) getClassDescriptor().getIdentifierDescriptor();

	}

	public ComponentAddress getBlockAddress(IPropertyDescriptor descriptor)
	{
		String blockName = descriptor.getName() + "ColumnValue";
		if (getPage().getComponents().containsKey(blockName))
		{
			return new ComponentAddress(getPage().getPageName(), blockName);
		} else
			return null;
	}

	public Object getSource()
	{
		return getInstances();
	}

	public List<TrailsTableColumn> getColumns()
	{
		return columns;
	}

	public void setColumns(List<TrailsTableColumn> columns)
	{
		this.columns = columns;
	}

/** // there is still issues with inherited binding.
 
	@Component(id = "table", type = "contrib:Table", inheritInformalParameters = true,
		bindings = {"row=object",
			"rowsClass=rowsClass",
			"column=column",
			"columnsClass=columnsClass",
			"index=index",
			"source=source",
			"columns=columns",
			"pageSize=pageSize",
			"initialSortColumn=initialSortColumn",
			"initialSortOrder=initialSortOrder",
			"persist=persist"})
	public abstract Table getTable();
**/

}