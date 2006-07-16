package org.trails.page;

import org.hibernate.criterion.DetachedCriteria;
import org.trails.callback.CallbackStack;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.i18n.ResourceBundleMessageSource;
import org.trails.persistence.PersistenceService;

public abstract class SearchBlockPage extends TrailsPage
{

    
    public abstract IPropertyDescriptor getDescriptor();

    public abstract void setDescriptor(IPropertyDescriptor Descriptor);

    public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria Criteria);
}
