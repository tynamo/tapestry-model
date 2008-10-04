package org.trails.component.search;

import java.lang.reflect.Constructor;

import org.apache.commons.lang.ClassUtils;

import org.apache.tapestry.form.translator.NumberTranslator;
import org.hibernate.criterion.Restrictions;
import org.trails.exception.TrailsRuntimeException;

public abstract class NumberSearchField extends SimpleSearchField
{

	public Object getTypeSpecificValue()
	{
		/**
		 * Warning: dorky code ahead. Need to take what will be some
		 * subclass Number and convert it to the right specific type.
		 * Since all Number subclasses have a String constructor, this will work
		 * @note: The Translator should take care of this.
		 **/
		try
		{
			Class type = ClassUtils.primitiveToWrapper(getPropertyDescriptor().getPropertyType());
			Constructor cons = type.getConstructor(new Class[]{String.class});
			return cons.newInstance(getValue().toString());
		}
		catch (Exception ex)
		{
			throw new TrailsRuntimeException(ex);
		}
	}

	public NumberTranslator getTranslator()
	{
		NumberTranslator numberTranslator = (NumberTranslator) getValidatorTranslatorService().getTranslator(getPropertyDescriptor());
		numberTranslator.setOmitZero(true);
		return numberTranslator;
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
