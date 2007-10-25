/*
 * Created on 25/11/2005 by Eduardo Piva - eduardo@gwe.com.br
 */
package org.trails.i18n;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

public class DefaultTrailsResourceBundleMessageSource extends AbstractResourceBundleMessageSource
{

	LocaleHolder localeHolder;
	private MessageSource messageSource;

	public String getMessage(String key)
	{
		return getMessage(key, getCurrentLocale());
	}

	public String getMessage(String key, Object[] args)
	{
		return getMessage(key, args, getCurrentLocale());
	}

	public String getMessage(String key, Locale locale)
	{
		return getMessage(key, EMPTY_ARGUMENTS, locale);
	}

	public String getMessage(String key, Object[] args, Locale locale)
	{
		try
		{
			return messageSource.getMessage(key, args, locale);
		} catch (NoSuchMessageException nsme)
		{
			return null;
		}
	}


	/**
	 * @todo remove if not needed
	 */
	private String getMessageWithLanguageLocale(String key, Object[] args, Locale locale)
	{
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

	/**
	 * @todo remove if not needed
	 */
	private String getMessageWithDefaultLocale(String key, Object[] args)
	{
		try
		{
			return messageSource.getMessage(key, args, Locale.ENGLISH);
		} catch (NoSuchMessageException nsme2)
		{
			return null;
		}
	}

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public void setLocaleHolder(LocaleHolder localeHolder)
	{
		this.localeHolder = localeHolder;
	}

	private Locale getCurrentLocale()
	{
		return localeHolder.getLocale();
	}
}
