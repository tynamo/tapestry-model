package org.trailsframework.validation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.MessageInterpolator;
import org.hibernate.validator.Validator;
import org.trailsframework.util.Utils;

public class HibernateClassValidatorFactory
{
/*
	private static Map classValidator = new HashMap();
	private TrailsMessageInterpolator messageInterpolator = new TrailsMessageInterpolator();
	TrailsMessageSource messageSource;
	LocaleHolder localeHolder;

	public void validateEntity(Object entity)
	{
		Locale locale = localeHolder.getLocale();

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


	public void setMessageSource(TrailsMessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public void setLocaleHolder(LocaleHolder localeHolder)
	{
		this.localeHolder = localeHolder;
	}

	*/
/**
	 * This inner class doesn't return exceptions when some key is searched in the bundle. This is nice so we don't have
	 * exceptions thrown in the screen by hibernate ClassValidator.
	 */
/*
	private class TrailsMessageInterpolator implements MessageInterpolator
	{
		public String interpolate(String key, Validator validator, MessageInterpolator messageInterpolator)
		{
			return messageSource.getMessageWithDefaultValue(key, new Object[]{validator}, key);
		}
	}
*/

}
