/*
 * Created on 25/11/2005 by Eduardo Piva - eduardo@gwe.com.br
 */
package org.trails.i18n;

import java.util.Locale;

import org.springframework.context.NoSuchMessageException;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

public class DefaultTrailsResourceBundleMessageSource implements ResourceBundleMessageSource
{

	private org.springframework.context.support.ResourceBundleMessageSource messageSource;
	private static final Object[] EMPTY_ARGUMENTS = new Object[]{};

	public String getMessage(String key, Locale locale)
	{
		try
		{
			return messageSource.getMessage(key, EMPTY_ARGUMENTS, locale);
		} catch (NoSuchMessageException nsme)
		{
			if (locale == null)
			{
				return getMessageWithNullLocale(key);
			} else
			{
				return getMessageWithLocale(key, locale);
			}
		}
	}

	public String getMessage(String key, Object[] args, Locale locale)
	{
		try
		{
			return messageSource.getMessage(key, args, locale);
		} catch (NoSuchMessageException nsme)
		{
			if (locale == null)
			{
				return getMessageWithNullLocale(key, args);
			} else
			{
				return getMessageWithLocale(key, args, locale);
			}
		}
	}

	public String getMessageWithDefaultValue(String key, Locale locale,
											 String defaultMessage)
	{
		String ret = getMessage(key, EMPTY_ARGUMENTS, locale);
		return (ret == null) ? defaultMessage : ret;
	}

	public String getMessageWithDefaultValue(String key, Object[] args, Locale locale, String defaultMessage)
	{
		String ret = getMessage(key, args, locale);
		return (ret == null) ? defaultMessage : ret;
	}

	public String getDisplayName(IDescriptor descriptor, Locale locale, String defaultMessage)
	{
		if (locale == null)
			return defaultMessage;

		String fullName;
		String shortName;
		if (descriptor instanceof IPropertyDescriptor)
		{
			IPropertyDescriptor property = (IPropertyDescriptor) descriptor;
			fullName = property.getBeanType().getName() + "." + property.getName();
			shortName = property.getName();
		} else if (descriptor instanceof IClassDescriptor)
		{
			IClassDescriptor clazz = (IClassDescriptor) descriptor;
			fullName = clazz.getType().getName();
			shortName = clazz.getType().getSimpleName();
		} else
		{
			return defaultMessage;
		}

		return selectDisplayName(fullName, shortName, defaultMessage, locale);
	}

	public String getPluralDislayName(IClassDescriptor clazz, Locale locale, String defaultMessage)
	{
		if (locale == null)
			return defaultMessage;

		String fullName = clazz.getType().getName() + "__plural";
		String shortName = clazz.getType().getSimpleName() + "__plural";

		return selectDisplayName(fullName, shortName, defaultMessage, locale);
	}

	// *** private methods
	/**
	 * Select a locale message, given two keys and a default value in case no message is found.
	 */
	private String selectDisplayName(String firstKey, String secondKey, String defValue, Locale locale)
	{
		String message;

		message = getMessage(firstKey, locale);
		if (message == null)
		{
			message = getMessage(secondKey, locale);
			if (message == null)
			{
				message = defValue;
			}
		}
		return message;
	}

	private String getMessageWithLocale(String key, Object[] args, Locale locale)
	{
		/* This will make us search for a less specific locale. Ex.: searching
		 * for pt_BR but using pt */
		try
		{
			Locale languageLocale = new Locale(locale.getLanguage());
			return messageSource.getMessage(key, args, languageLocale);
		} catch (NoSuchMessageException nsme2)
		{
			/* Try a default locale */
			try
			{
				return messageSource.getMessage(key, args, Locale.ENGLISH);
			} catch (NoSuchMessageException nsme3)
			{
				return null;
			}
		}
	}

	private String getMessageWithNullLocale(String key, Object[] args)
	{
		/* Try to search an english locale */
		try
		{
			return messageSource.getMessage(key, args, Locale.ENGLISH);
		} catch (NoSuchMessageException nsme2)
		{
			return null;
		}
	}


	private String getMessageWithLocale(String key, Locale locale)
	{
		return getMessageWithLocale(key, EMPTY_ARGUMENTS, locale);
	}

	private String getMessageWithNullLocale(String key)
	{
		return getMessageWithNullLocale(key, EMPTY_ARGUMENTS);
	}

	// *** setters
	public void setMessageSource(org.springframework.context.support.ResourceBundleMessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

}
