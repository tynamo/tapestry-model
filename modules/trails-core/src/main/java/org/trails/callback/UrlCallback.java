package org.trails.callback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.callback.ICallback;

/**
 * A callback for returning to a given URL.
 */
public class UrlCallback implements ICallback
{

	private static final Log LOG = LogFactory.getLog(UrlCallback.class);
	private String url;

	public UrlCallback(String url)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Creating redirect callback for: " + url);
		}

		this.url = url;
	}

	public void performCallback(IRequestCycle iRequestCycle)
	{
		throw new RedirectException(url);
	}
}