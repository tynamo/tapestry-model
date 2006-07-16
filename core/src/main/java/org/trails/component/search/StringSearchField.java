package org.trails.component.search;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.validation.ValidatorTranslatorService;

public abstract class StringSearchField extends SimpleSearchField
{

	@Override
	public void buildCriterion()
	{
		if (getValue() != null)
		{
			getCriteria().add(Restrictions.like(getPropertyDescriptor().getName(), (String)getValue(), MatchMode.ANYWHERE));
		}		
	}



}
