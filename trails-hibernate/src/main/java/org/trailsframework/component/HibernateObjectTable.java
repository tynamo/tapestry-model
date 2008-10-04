package org.trails.component;

import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.contrib.table.model.common.AbstractTableColumn;
import org.apache.tapestry.contrib.table.model.common.BlockTableRendererSource;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.descriptor.extension.BlobDescriptorExtension;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.HibernatePersistenceService;

@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class HibernateObjectTable extends ObjectTable
{

	public static final String BLOB_COLUMN = "blob" + AbstractTableColumn.VALUE_RENDERER_BLOCK_SUFFIX;

	@InjectObject("service:trails.hibernate.PersistenceService")
	public abstract HibernatePersistenceService getHibernatePersistenceService();

	/**
	 * @return
	 * @todo: remove when the components reuse issue goes away.
	 */
	public HibernatePersistenceService getPersistenceService()
	{
		return getHibernatePersistenceService();
	}

	@Parameter
	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria criteria);

	public Object getSource()
	{
		if (getInstances() == null)
		{
			return new HibernateTableModel(getClassDescriptor().getType(), getHibernatePersistenceService(),
					getCriteria());
		}
		return getInstances();
	}

	protected void alterTableColumn(IPropertyDescriptor descriptor, TrailsTableColumn tableColumn)
	{
		if (descriptor.supportsExtension(BlobDescriptorExtension.class))
		{
			tableColumn.setValueRendererSource(new BlockTableRendererSource((Block) getComponent(BLOB_COLUMN)));
		}
	}
}
