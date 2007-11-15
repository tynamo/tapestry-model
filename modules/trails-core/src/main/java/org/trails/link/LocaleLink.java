/*
 * Created on 30/11/2005
 *
 */
package org.trails.link;

import java.util.Locale;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.trails.servlet.TrailsApplicationServlet;

public abstract class LocaleLink extends BaseComponent
{

	public abstract String getLanguage();

	public abstract String getLinkText();

	public abstract String getCountry();

	public void click(IRequestCycle cycle)
	{
		Locale locale = new Locale(getLanguage(), getCountry());
		getPage().getEngine().setLocale(locale);
		cycle.cleanup();
		throw new PageRedirectException(getPage());
	}


}
