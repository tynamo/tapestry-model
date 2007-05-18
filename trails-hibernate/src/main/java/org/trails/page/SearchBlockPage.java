package org.trails.page;

import org.hibernate.criterion.DetachedCriteria;
import org.trails.descriptor.IPropertyDescriptor;

public abstract class SearchBlockPage extends TrailsPage
{


	public abstract IPropertyDescriptor getDescriptor();

	public abstract void setDescriptor(IPropertyDescriptor Descriptor);

	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria Criteria);
}
