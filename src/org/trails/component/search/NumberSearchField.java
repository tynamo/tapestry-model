package org.trails.component.search;

import java.lang.reflect.Constructor;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.TrailsRuntimeException;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.validation.ValidatorTranslatorService;

public abstract class NumberSearchField extends SimpleSearchField
{

	public Object getTypeSpecificValue()
	{
		// Warning: dorky code ahead. Need to take what will be some
		// subclass Number and convert it to the right specific type.
		// Since all Number subclasses have a String constructor, this will work
		try
		{
			Class type = getPropertyDescriptor().getPropertyType();
			Constructor cons = type.getConstructor(new Class[] {String.class});
			return cons.newInstance(getValue().toString());
		}
		catch (Exception ex)
		{
			throw new TrailsRuntimeException(ex);
		}
	}

	@Override
	public void buildCriterion()
	{
		if (getValue() != null)
		{
			getCriteria().add(Restrictions.eq(getPropertyDescriptor().getName(), getTypeSpecificValue()));
		}
	}
	
	

}
