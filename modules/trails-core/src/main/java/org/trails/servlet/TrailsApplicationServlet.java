/*
 * Created on 29/11/2005
 *
 */
package org.trails.servlet;

import java.util.Locale;
import javax.servlet.ServletConfig;

import org.apache.hivemind.Registry;
import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.services.RequestLocaleManager;

/**
 * This class will expose the Tapestry Registry as an static atribute.
 *
 * @author Eduardo Fernandes Piva (eduardo@gwe.com.br)
 */
public class TrailsApplicationServlet extends ApplicationServlet
{

	/**
	 * This is used to share the Registry among all the
	 */
	private static Registry tapestryRegistry = null;

	@Override
	protected Registry constructRegistry(ServletConfig config)
	{
		synchronized (TrailsApplicationServlet.class)
		{
			TrailsApplicationServlet.tapestryRegistry = super.constructRegistry(config);
			return TrailsApplicationServlet.tapestryRegistry;
		}
	}

	@Override
	public void destroy()
	{
		synchronized (TrailsApplicationServlet.class)
		{
			super.destroy();
			TrailsApplicationServlet.tapestryRegistry = null;
		}
	}

	/*
		 * Used by Spring.
		 */
	public static Registry getRegistry()
	{
		return tapestryRegistry;
	}

	public static Locale getCurrentLocale()
	{
		return ((ThreadLocale) tapestryRegistry.getService("hivemind.ThreadLocale", ThreadLocale.class)).getLocale();
	}
}
