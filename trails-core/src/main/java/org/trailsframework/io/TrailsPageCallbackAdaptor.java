package org.trails.io;

import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.trails.callback.TrailsPageCallback;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.engine.TrailsPagesServiceParameter;
import org.trails.engine.SqueezableTrailsPagesServiceParameter;
import org.trails.page.PageType;

public class TrailsPageCallbackAdaptor implements SqueezeAdaptor
{

	public static final String PREFIX = "N";
	private IEngineService trailsPagesService;

	public String getPrefix()
	{
		return PREFIX;
	}

	public Class getDataClass()
	{
		return TrailsPageCallback.class;
	}

	public String squeeze(DataSqueezer squeezer, Object data)
	{
		TrailsPagesServiceParameter tpsp = ((TrailsPageCallback) data).getTpsp();

		SqueezableTrailsPagesServiceParameter serviceParameter = new SqueezableTrailsPagesServiceParameter(tpsp.getPageType().name(),
				squeezer.squeeze(tpsp.getClassDescriptor()), squeezer.squeeze(tpsp.getModel()),
				squeezer.squeeze(tpsp.getAssociationDescriptor()), squeezer.squeeze(tpsp.getParent()));
		return PREFIX + squeezer.squeeze(serviceParameter);
	}

	public Object unsqueeze(DataSqueezer squeezer, String string)
	{
		SqueezableTrailsPagesServiceParameter adp = (SqueezableTrailsPagesServiceParameter) squeezer.unsqueeze(string.substring(PREFIX.length()));
		return new TrailsPageCallback(
				new TrailsPagesServiceParameter(PageType.valueOf(adp.getPageType()), (IClassDescriptor)
						squeezer.unsqueeze(adp.getClassDescriptor()), squeezer.unsqueeze(adp.getModel()),
						(CollectionDescriptor) squeezer.unsqueeze(adp.getAssoc()),
						squeezer.unsqueeze(adp.getParent())), trailsPagesService);
	}

	public void setTrailsPagesService(IEngineService trailsPagesService)
	{
		this.trailsPagesService = trailsPagesService;
	}
}
