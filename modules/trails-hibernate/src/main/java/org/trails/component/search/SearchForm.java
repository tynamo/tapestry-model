package org.trails.component.search;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.hivemind.util.PropertyUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.component.ClassDescriptorComponent;
import org.trails.descriptor.BlockFinder;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.page.HibernateListPage;
import org.trails.page.PageResolver;
import org.trails.page.TrailsPage.PageType;

public abstract class SearchForm extends ClassDescriptorComponent implements PageBeginRenderListener
{

	@InjectObject("service:trails.core.PageResolver")
	public abstract PageResolver getPageResolver();

	@InjectObject("spring:searchBlockFinder")
	public abstract BlockFinder getBlockFinder();

	@Parameter(name = "classDescriptor", required = false, defaultValue = "ognl:page.classDescriptor")
	public abstract IClassDescriptor getClassDescriptor();

	public abstract void setClassDescriptor(IClassDescriptor ClassDescriptor);

	public abstract IPropertyDescriptor getPropertyDescriptor();

	public abstract void setPropertyDescriptor(IPropertyDescriptor PropertyDescriptor);

	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria Criteria);

	public void search(IRequestCycle cycle)
	{
		HibernateListPage listPage = (HibernateListPage) getPageResolver().resolvePage(cycle,
			getClassDescriptor().getType(),
			PageType.List);
		listPage.setCriteria(getCriteria());
		cycle.activate(listPage);
	}

	public Block getBlock()
	{
		Block searchBlock = getBlockFinder().findBlock(getPage().getRequestCycle(), getPropertyDescriptor());
		PropertyUtils.write(searchBlock.getPage(), "criteria", getCriteria());
		return searchBlock;
	}

	public void pageBeginRender(PageEvent event)
	{
		setCriteria(DetachedCriteria.forClass(getClassDescriptor().getType()));
	}

}
