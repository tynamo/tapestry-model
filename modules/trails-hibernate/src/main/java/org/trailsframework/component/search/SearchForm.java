package org.trails.component.search;

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.builder.BuilderDirector;
import org.trails.component.ClassDescriptorComponent;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.finder.BlockFinder;
import org.trails.page.HibernateListPage;
import org.trails.page.PageResolver;
import org.trails.page.PageType;

/**
 * This component extends Form and renders a form to search for a domain object
 */
@ComponentClass
public abstract class SearchForm extends ClassDescriptorComponent implements PageBeginRenderListener
{
	private static final String MODEL_PARAMETER = "model";

	@InjectObject("service:trails.core.PageResolver")
	public abstract PageResolver getPageResolver();

	@InjectObject("spring:searchBlockFinder")
	public abstract BlockFinder getBlockFinder();

	@InjectObject("service:trails.core.BuilderDirector")
	public abstract BuilderDirector getBuilderDirector();

	@Parameter(name = MODEL_PARAMETER, required = false)
	public abstract Object getModelParameter();
	public abstract void setModelParameter(Object model);

	public abstract Object getModelLocalProperty();
	public abstract void setModelLocalProperty(Object model);

	public abstract IPropertyDescriptor getPropertyDescriptor();
	public abstract void setPropertyDescriptor(IPropertyDescriptor PropertyDescriptor);

	public abstract DetachedCriteria getCriteria();
	public abstract void setCriteria(DetachedCriteria Criteria);

	public Object getModel()
	{
		return isParameterBound(MODEL_PARAMETER) ? getModelParameter() : getModelLocalProperty();
	}

	public void setModel(Object model)
	{
		if (isParameterBound(MODEL_PARAMETER))
		{
			setModelParameter(model);
		} else
		{
			setModelLocalProperty(model);
		}
	}

	public void search(IRequestCycle cycle)
	{
		HibernateListPage listPage = (HibernateListPage) getPageResolver().resolvePage(cycle,
			getClassDescriptor().getType(),
			PageType.LIST);
		listPage.setClassDescriptor(getClassDescriptor());
		listPage.setCriteria(getCriteria());
		cycle.activate(listPage);
	}

	public Block getBlock()
	{
		Block searchBlock = getBlockFinder().findBlock(getPage().getRequestCycle(), getPropertyDescriptor());
		PropertyUtils.write(searchBlock.getPage(), MODEL_PARAMETER, getModel());
		PropertyUtils.write(searchBlock.getPage(), "criteria", getCriteria());
		return searchBlock;
	}

	public void pageBeginRender(PageEvent event)
	{
		if (getModel() == null)
		{
			setModel(getBuilderDirector().createNewInstance(getClassDescriptor().getType()));
		}

		setCriteria(DetachedCriteria.forClass(getClassDescriptor().getType()));
	}

}
