package org.trails.component.search;

import java.util.Iterator;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.component.ClassDescriptorComponent;
import org.trails.page.ListPage;
import org.trails.page.PageResolver;
import org.trails.page.TrailsPage.PageType;

public abstract class SearchForm extends ClassDescriptorComponent
{
	@InjectObject("spring:pageResolver")
	public abstract PageResolver getPageResolver();
	
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

	public void search(IRequestCycle cycle)
	{
		ListPage listPage = (ListPage)getPageResolver().resolvePage(cycle, 
				getClassDescriptor().getType().getName(),
				PageType.LIST);
		listPage.setCriteria(buildCriteria());
		cycle.activate(listPage);
	}
	
}
