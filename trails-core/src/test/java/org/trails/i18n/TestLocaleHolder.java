package org.trails.i18n;

import java.util.Locale;


public class TestLocaleHolder implements LocaleHolder
{

	Locale locale = Locale.ENGLISH;

	public TestLocaleHolder()
	{
	}

	public TestLocaleHolder(Locale locale)
	{
		this.locale = locale;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}
}