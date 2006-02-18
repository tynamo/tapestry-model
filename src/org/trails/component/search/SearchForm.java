package org.trails.component.search;

import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IComponent;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.descriptor.IClassDescriptor;

public abstract class SearchForm extends BaseComponent
{

	public abstract IClassDescriptor getClassDescriptor();

	public abstract void setClassDescriptor(IClassDescriptor ClassDescriptor);
	
	/**
	 * Asks each component to add its criterion
	 * @return
	 */
	public DetachedCriteria buildCriteria()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(getClassDescriptor().getType());
		for (Iterator iter = getComponents().values().iterator(); iter.hasNext();)
		{
			IComponent component = (IComponent)iter.next();
			if (component instanceof SearchComponent)
			{
				criteria.add(((SearchComponent)component).buildCriterion());
			}
		}
		return criteria;
	}
	
}
