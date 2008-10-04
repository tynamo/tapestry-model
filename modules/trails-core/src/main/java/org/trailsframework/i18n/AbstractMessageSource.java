package org.trails.i18n;

import java.util.Locale;

import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

public abstract class AbstractMessageSource implements TrailsMessageSource
{

	public String getMessageWithDefaultValue(String key, String defaultMessage)
	{
		return getMessageOrDefaultValue(getMessage(key), defaultMessage);
	}

	public String getMessageWithDefaultValue(String key, Object[] args, String defaultMessage)
	{
		return getMessageOrDefaultValue(getMessage(key, args), defaultMessage);
	}

	public String getMessageWithDefaultValue(String key, Locale locale, String defaultMessage)
	{
		return getMessageOrDefaultValue(getMessage(key, locale), defaultMessage);
	}

	public String getMessageWithDefaultValue(String key, Object[] args, Locale locale, String defaultMessage)
	{
		return getMessageOrDefaultValue(getMessage(key, args, locale), defaultMessage);
	}

	private String getMessageOrDefaultValue(String message, String defaultMessage)
	{
		return (message != null) ? message : defaultMessage;
	}

	public String getDisplayName(IDescriptor descriptor, String defaultMessage)
	{
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

		return selectDisplayName(fullName, shortName, defaultMessage);
	}

	public String getPluralDislayName(IClassDescriptor clazz, String defaultMessage)
	{
		String fullName = clazz.getType().getName() + "__plural";
		String shortName = clazz.getType().getSimpleName() + "__plural";

		return selectDisplayName(fullName, shortName, defaultMessage);
	}

	/**
	 * Select a locale message, given two keys and a default value in case no message is found.
	 */
	private String selectDisplayName(String firstKey, String secondKey, String defValue)
	{
		String message;

		message = getMessage(firstKey);
		if (message == null)
		{
			message = getMessage(secondKey);
			if (message == null)
			{
				message = defValue;
			}
		}
		return message;
	}
}
