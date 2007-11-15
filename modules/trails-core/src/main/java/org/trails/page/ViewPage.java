package org.trails.page;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.trails.callback.UrlCallback;
import org.trails.engine.TrailsPagesServiceParameter;

public abstract class ViewPage extends ModelPage implements IExternalPage
{

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
	{
		setModel(parameters[0]);
	}

	public void pushCallback()
	{
		UrlCallback callback = new UrlCallback(getTrailsPagesService().getLink(false, new TrailsPagesServiceParameter(PageType.EDIT, getClassDescriptor(), getModel())).getURL());
		if (getCallbackStack() != null && (getCallbackStack().isEmpty() || !getCallbackStack().peek().equals(callback)))
		{
			getCallbackStack().push(callback);
		}
	}
}