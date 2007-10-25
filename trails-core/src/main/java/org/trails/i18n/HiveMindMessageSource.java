package org.trails.i18n;

import java.util.Locale;

import org.apache.hivemind.Messages;
import org.apache.hivemind.service.ThreadLocale;

public class HiveMindMessageSource extends AbstractMessageSource
{

	private ThreadLocale threadLocale;
	private Messages messageSource;

	public String getMessage(String key)
	{
		return messageSource.getMessage(key);
	}

	public String getMessage(String key, Object[] args)
	{
		return messageSource.format(key, args);
	}

	public String getMessage(String key, Locale locale)
	{
		Locale currentLocale = threadLocale.getLocale();
		/** I know!!, awful hack! **/
		threadLocale.setLocale(locale);
		String message = getMessage(key);
		threadLocale.setLocale(currentLocale);
		return message;
	}

	public String getMessage(String key, Object[] args, Locale locale)
	{
		Locale currentLocale = threadLocale.getLocale();
		/** I know!!, awful hack! **/
		threadLocale.setLocale(locale);
		String message = getMessage(key, args);
		threadLocale.setLocale(currentLocale);
		return message;
	}

	public void setThreadLocale(ThreadLocale threadLocale)
	{
		this.threadLocale = threadLocale;
	}

	public void setMessageSource(Messages messageSource)
	{
		this.messageSource = messageSource;
	}

}