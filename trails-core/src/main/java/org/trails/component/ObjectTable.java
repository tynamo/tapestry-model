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
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.contrib.table.components.TableMessages;
import org.apache.tapestry.contrib.table.components.TableView;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.common.AbstractTableColumn;
import org.apache.tapestry.contrib.table.model.common.BlockTableRendererSource;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumnModel;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Produces a table for the list of instances
 */
//@GlobalComponent
@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class ObjectTable extends ClassDescriptorComponent implements PageBeginRenderListener
{
	private static final Log LOG = LogFactory.getLog(ObjectTable.class);

	public static final String LINK_COLUMN = "link" + AbstractTableColumn.VALUE_RENDERER_BLOCK_SUFFIX;

	private ITableColumnModel m_objColumnModel = null;

	@Parameter(defaultValue = "ognl:@java.lang.System@getProperty('org.apache.tapestry.disable-caching')")
	public abstract boolean isCacheDisabled();

	protected List<ITableColumn> columns;

	@Parameter(required = false, defaultValue = "false", cache = true)
	public abstract boolean isShowCollections();

	public abstract void setShowCollections(boolean ShowCollections);

	@InjectObject("service:trails.core.PersistenceService")
	public abstract PersistenceService getPersistenceService();

	/**
	 * The data to be displayed by the component.
	 *
	 * @return
	 */
	@Parameter
	public abstract List getInstances();

	public abstract void setInstances(List instances);

	/**
	 * If provided, the parameter is updated with the current row being rendered.
	 */
	@Parameter(cache = false, defaultValue = "objectParameter")
	public abstract Object getObject();

	/**
	 * Returns the currently rendered table row. It's used when there is no binding for the "object" parameter. This method
	 * is for internal use only.
	 */
	public abstract Object getObjectParameter();
	public abstract void setObjectParameter(Object object);

	/**
	 * The object representing the current column being rendered.
	 */
	@Parameter(cache = false)
	public abstract ITableColumn getColumn();

	/**
	 * The CSS class of the table rows.
	 */
	@Parameter(cache = false)
	public abstract String getRowsClass();

	/**
	 * The CSS class of the table columns.
	 */
	@Parameter(cache = false)
	public abstract String getColumnsClass();

	/**
	 * The number of records displayed per page.
	 */
	@Parameter(cache = false, defaultValue = "10")
	public abstract int getPageSize();

	/**
	 * If provided, the parameter is updated with the index of the loop on each iteration.
	 */
	@Parameter
	public abstract int getIndex();

	/**
	 * The id of the column to initially sort the table by.
	 */
	@Parameter
	public abstract String getInitialSortColumn();

	/**
	 * The order of the initial sorting. Set this parameter to 'false' to sort in an ascending order and to 'true' to sort
	 * in a descending one.
	 */
	@Parameter(defaultValue = "false")
	public abstract boolean getInitialSortOrder();

	/**
	 * Defines how the table state (paging and sorting) will be persisted if no tableSessionStoreManager is defined. The
	 * possible values are 'session' (the default), 'client', 'client:page', and 'client:app'.
	 */
	@Parameter(defaultValue = "literal:session")
	public abstract String getPersist();

	/**
	 * The image to use to describe a column sorted in a descending order.
	 */
	@Parameter
	public abstract IAsset getArrowDownAsset();

	/**
	 * The image to use to describe a column sorted in an ascending order.
	 */
	@Parameter
	public abstract IAsset getArrowUpAsset();

	/**
	 * @return The table columns to be displayed.
	 */
	public List<ITableColumn> getColumns()
	{
		return !isCacheDisabled() && getColumnsCache() != null ? getColumnsCache() : columns;
	}

	@Persist
	public abstract List<ITableColumn> getColumnsCache();

	public abstract void setColumnsCache(List<ITableColumn> columns);

	/**
	 * This parameter provides a new set of columns to be displayed after the ones provided by the IClassDescriptor or
	 * EditProperties parameters. The parameter works like the [<a href="http://tapestry.apache.org/tapestry4.1/tapestry-contrib/componentreference/table.html">contrib
	 * table</a>] 'columns' parameter. The parameter must be an array, a list, or an Iterator of ITableColumn objects, an
	 * ITableColumnModel, or a String describing the columns (see documentation).
	 */
	@Parameter
	public abstract Object getExtraColumns();

	public abstract void setExtraColumns(Object o);

	@InjectObject("service:tapestry.ognl.ExpressionEvaluator")
	public abstract ExpressionEvaluator getEvaluator();

	@Override
	protected void prepareForRender(IRequestCycle cycle)
	{
		/**
		 * If getColumnsCache() == null means that pageBeginRender wasn't fired because the component is inside a Block
		 * in another page.
		 */
		if (getColumnsCache() == null || isCacheDisabled())
		{
			columns = createColumns();
		}
		super.prepareForRender(cycle);
	}

	public void pageBeginRender(PageEvent event)
	{
		if (!event.getRequestCycle().isRewinding())
		{
			setColumnsCache(createColumns());
		}
	}

	/**
	 * It creates a {@link ITableColumn} list out of the IClassDescriptor metadata. It's meant to be used as a {@link
	 * ITableColumnModel} by the inner table.
	 *
	 * @return A {@link ITableColumn} list
	 */
	private List<ITableColumn> createColumns()
	{
		List<ITableColumn> columns = new ArrayList<ITableColumn>();
		if (getClassDescriptor() != null)
		{
			LOG.debug("Creating Columns for " + getClassDescriptor().getPluralDisplayName());
			for (IPropertyDescriptor descriptor : getPropertyDescriptors())
			{
				TrailsTableColumn tableColumn = new TrailsTableColumn(descriptor, getEvaluator());
				tableColumn.loadSettings(getContainer());
				columns.add(tableColumn);
				Block block = (Block) getContainer().getComponents().get(
						descriptor.getName() + AbstractTableColumn.VALUE_RENDERER_BLOCK_SUFFIX);
				if (block == null)
				{
					if (getLinkProperty().equals(descriptor.getName())
							&& (getClassDescriptor().isAllowSave() || getClassDescriptor().isAllowRemove()))
					{
						/**
						 * Add a link for the edit page following these rules:
						 * a) Use the identifier descriptor if is displayable (summary=true ).
						 * b) Use the first displayable property if  the identifier property is not displayable
						 *    (summary=false)
						 *
						 */
						Block linkBlock = (Block) getContainer().getComponents().get(LINK_COLUMN);
						tableColumn.setValueRendererSource(new BlockTableRendererSource(
								linkBlock != null ? linkBlock : (Block) getComponent(LINK_COLUMN)));
					} else
					{
						alterTableColumn(descriptor, tableColumn);
					}
				}
			}
			if (getExtraColumns() != null)
			{
				addAll(columns, getTableColumnModel().getColumns());
			}
		} else
		{
			LOG.warn("NULL ClassDescriptor");
		}
		return columns;
	}

	/**
	 * Hook method to allow subclasses to modify the tableColumn,
	 *
	 * @param descriptor
	 * @param tableColumn
	 */
	protected void alterTableColumn(IPropertyDescriptor descriptor, TrailsTableColumn tableColumn)
	{

	}

	protected boolean shouldDisplay(IPropertyDescriptor descriptor)
	{
		return !descriptor.isHidden() && descriptor.isSummary() &&
				((descriptor.isCollection() && isShowCollections()) || (!descriptor.isCollection()));
	}

	/**
	 * @return
	 */
	public String getIdentifierProperty()
	{
		return this.getIdentifierPropertyDescriptor().getName();

	}

	/**
	 * Returns the name of the property to be used as link to the editor. If the default Id property is not displayable
	 * then return the name of the first displayable property.
	 *
	 * @return
	 */
	public String getLinkProperty()
	{
		IPropertyDescriptor propertyDescriptor = getIdentifierPropertyDescriptor();
		if (!shouldDisplay(propertyDescriptor))
			propertyDescriptor = getFirstDisplayableProperty();

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
			if (shouldDisplay(descriptor))
			{
				return descriptor;

			}
		}

		return null; // If we get here, that means we have no displayable descriptors
		// TODO check if we should throw an exception instead
	}

	/**
	 * @return
	 */
	protected IPropertyDescriptor getIdentifierPropertyDescriptor()
	{
		return (IPropertyDescriptor) getClassDescriptor().getIdentifierDescriptor();

	}

	/**
	 * It provides the source parameter to the inner [<a href="http://tapestry.apache.org/tapestry4.1/tapestry-contrib/componentreference/table.html">table
	 * </a>]
	 *
	 * @return The data to be displayed by the inner table.
	 */
	public Object getSource()
	{
		return getInstances();
	}

	/**
	 * Returns the table column model as specified by the 'columns' binding. If the value of the 'columns' binding is of a
	 * type different than ITableColumnModel, this method makes the appropriate conversion.
	 *
	 * @return The table column model as specified by the 'columns' binding
	 */
	protected ITableColumnModel getTableColumnModel()
	{
		Object objColumns = getExtraColumns();

		if (objColumns == null) return null;

		if (objColumns instanceof ITableColumnModel)
		{
			return (ITableColumnModel) objColumns;
		}

		if (objColumns instanceof Iterator)
		{
			// convert to List
			Iterator objColumnsIterator = (Iterator) objColumns;
			List arrColumnsList = new ArrayList();
			addAll(arrColumnsList, objColumnsIterator);
			objColumns = arrColumnsList;
		}

		if (objColumns instanceof List)
		{
			// validate that the list contains only ITableColumn instances
			List arrColumnsList = (List) objColumns;
			int nColumnsNumber = arrColumnsList.size();
			for (int i = 0; i < nColumnsNumber; i++)
			{
				if (!(arrColumnsList.get(i) instanceof ITableColumn))
					throw new ApplicationRuntimeException(columnsOnlyPlease(this));
			}
			// objColumns = arrColumnsList.toArray(new
			// ITableColumn[nColumnsNumber]);
			return new SimpleTableColumnModel(arrColumnsList);
		}

		if (objColumns instanceof ITableColumn[])
		{
			return new SimpleTableColumnModel(
					(ITableColumn[]) objColumns);
		}

		if (objColumns instanceof String)
		{
			String strColumns = (String) objColumns;
			if (getBinding("extraColumns").isInvariant())
			{
				// if the binding is invariant, create the columns only once
				if (m_objColumnModel == null)
					m_objColumnModel = generateTableColumnModel(strColumns);
				return m_objColumnModel;
			}

			// if the binding is not invariant, create them every time
			return generateTableColumnModel(strColumns);
		}
		throw new ApplicationRuntimeException(invalidTableColumns(this, objColumns));
	}

	/**
	 * Generate a table column model out of the description string provided. Entries in the description string are
	 * separated by commas. Each column entry is of the format name, name:expression, or name:displayName:expression. An
	 * entry prefixed with ! represents a non-sortable column. If the whole description string is prefixed with *, it
	 * represents columns to be included in a Form.
	 *
	 * @param strDesc the description of the column model to be generated
	 * @return a table column model based on the provided description
	 */
	protected ITableColumnModel generateTableColumnModel(String strDesc)
	{
		return getTableViewComponent().getModelSource().generateTableColumnModel(
				getTableViewComponent().getColumnSource(), strDesc, getTableViewComponent(), getContainer());
	}

	private void addAll(List arrColumnsList, Iterator objColumnsIterator)
	{
		while (objColumnsIterator.hasNext())
			arrColumnsList.add(objColumnsIterator.next());
	}

	protected TableView getTableViewComponent()
	{
		return (TableView) getComponent("table").getComponent("tableView");
	}

	private static final MessageFormatter _formatter = new MessageFormatter(TableMessages.class);

	static String columnsOnlyPlease(IComponent component)
	{
		return _formatter.format("columns-only-please", component.getExtendedId());
	}

	static String invalidTableColumns(IComponent component, Object columnSource)
	{
		return _formatter.format("invalid-table-column", component.getExtendedId(),
				ClassFabUtils.getJavaClassName(columnSource.getClass()));
	}

/*
	// there are still issues with inherited binding.
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
*/


}