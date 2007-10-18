package org.trails.page;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

public abstract class ViewPage extends ModelPage implements IExternalPage
{

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
	{
		setModel(parameters[0]);
	}

	public void pushCallback()
	{
		//do nothing;
	}
}