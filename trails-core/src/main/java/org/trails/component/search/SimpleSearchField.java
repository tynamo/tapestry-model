package org.trails.component.search;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.validation.ValidatorTranslatorService;

public abstract class SimpleSearchField extends BaseComponent
{
	public abstract Object getValue();

	public abstract void setValue(Object Value);
	
	@Parameter(required=false, defaultValue="page.criteria")
	public abstract DetachedCriteria getCriteria();

	public abstract void setCriteria(DetachedCriteria Criteria);
	
	@Parameter(required=true)
	public abstract IPropertyDescriptor getPropertyDescriptor();

	public abstract void setPropertyDescriptor(IPropertyDescriptor PropertyDescriptor);
	
	@InjectObject("spring:validatorTranslatorService")
	public abstract ValidatorTranslatorService getValidatorTranslatorService();

	public void buildCriterion()
	{
		if (getValue() != null)
		{
			getCriteria().add(Restrictions.eq(getPropertyDescriptor().getName(), getValue()));
		}
	}

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
	{
		// TODO Auto-generated method stub
		super.renderComponent(writer, cycle);
		if (cycle.isRewinding())
		{
			buildCriterion();
		}
	}
	
	
}
