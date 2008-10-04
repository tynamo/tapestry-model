package org.trails.page;

import org.apache.tapestry.callback.ICallback;
import org.trails.callback.UrlCallback;
import org.trails.engine.TrailsPagesServiceParameter;

public abstract class ViewPage extends ModelPage
{
	protected ICallback callbackToThisPage()
	{
		return new UrlCallback(getTrailsPagesService().getLink(false, new TrailsPagesServiceParameter(PageType.VIEW, getClassDescriptor(), getModel())).getURL());
	}
}