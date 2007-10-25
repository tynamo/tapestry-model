package org.trails.i18n;

import java.util.Locale;

import org.trails.servlet.TrailsApplicationServlet;

public class SpringLocaleHolder implements LocaleHolder
{
	public Locale getLocale()
	{
		return TrailsApplicationServlet.getCurrentLocale();
	}
}
