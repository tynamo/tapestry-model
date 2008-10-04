package org.trails.callback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.engine.IEngineService;
import org.trails.engine.TrailsPagesServiceParameter;

/**
 * A callback for returning to a given TrailsPage.
 */
public class TrailsPageCallback implements ICallback
{

	private static final Log LOG = LogFactory.getLog(TrailsPageCallback.class);
	private TrailsPagesServiceParameter tpsp;
	private IEngineService trailsPagesService;

	public TrailsPageCallback(TrailsPagesServiceParameter tpsp, IEngineService trailsPagesService)
	{
		this.tpsp = tpsp;
		this.trailsPagesService = trailsPagesService;
	}

	public void performCallback(IRequestCycle iRequestCycle)
	{
		throw new RedirectException(trailsPagesService.getLink(false, tpsp).getURL());
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TrailsPageCallback that = (TrailsPageCallback) o;

		return tpsp != null ? tpsp.equals(that.tpsp) : that.tpsp == null;

	}

	public int hashCode()
	{
		return (tpsp != null ? tpsp.hashCode() : 0);
	}

	public TrailsPagesServiceParameter getTpsp()
	{
		return tpsp;
	}
}