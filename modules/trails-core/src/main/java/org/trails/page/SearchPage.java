package org.trails.page;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry.IRequestCycle;
import org.trails.exception.TrailsRuntimeException;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchPage extends TrailsPage
{

	public void activateTrailsPage(Object[] objects, IRequestCycle iRequestCycle)
	{
		if (getCallbackStack() != null)
		{
			getCallbackStack().clear();
		}
	}
}
