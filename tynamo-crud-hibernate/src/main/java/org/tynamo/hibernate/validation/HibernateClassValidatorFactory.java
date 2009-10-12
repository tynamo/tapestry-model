package org.tynamo.hibernate.validation;

import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.hibernate.validator.*;
import org.tynamo.util.Utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HibernateClassValidatorFactory
{

	private static Map classValidator = new HashMap();
	private TrailsMessageInterpolator messageInterpolator = new TrailsMessageInterpolator();
	ThreadLocale threadLocale;

	public HibernateClassValidatorFactory(ThreadLocale threadLocale)
	{
		this.threadLocale = threadLocale;
	}

	public void validateEntity(Object entity)
	{
		Locale locale = threadLocale.getLocale();

		String key = Utils.checkForCGLIB(entity.getClass()).toString() + "locale:" + locale;
		ClassValidator validator = (ClassValidator) classValidator.get(key);
		if (validator == null)
		{
			validator = initializeCache(key, entity, locale);
		}

		InvalidValue[] invalidValues = validator.getInvalidValues(entity);
		if (invalidValues.length > 0)
		{
			throw new InvalidStateException(invalidValues);
		}

	}

	private ClassValidator initializeCache(String key, Object entity, Locale locale)
	{
		Class entityClass = Utils.checkForCGLIB(entity.getClass());
		ClassValidator validator;
		if (locale == null)
		{
			validator = new ClassValidator(entityClass);
		} else
		{
			validator = new ClassValidator(entityClass, messageInterpolator);
		}

		classValidator.put(key, validator);
		return validator;
	}


	/**
	 * This inner class doesn't return exceptions when some key is searched in the bundle. This is nice so we don't have
	 * exceptions thrown in the screen by hibernate ClassValidator.
	 */
	private class TrailsMessageInterpolator implements MessageInterpolator
	{
		public String interpolate(String key, Validator validator, MessageInterpolator messageInterpolator)
		{
			return key;
		}
	}

}
