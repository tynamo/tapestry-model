package org.trails.component.search;

import org.apache.tapestry.BaseComponent;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.trails.descriptor.IPropertyDescriptor;

public abstract class SimpleSearchField extends BaseComponent implements SearchComponent
{
	public abstract Object getValue();

	public abstract void setValue(Object Value);
	
	public abstract IPropertyDescriptor getPropertyDescriptor();

	public abstract void setPropertyDescriptor(IPropertyDescriptor PropertyDescriptor);

	public Criterion buildCriterion()
	{
		return Restrictions.eq(getPropertyDescriptor().getName(), getValue());
	}
}
