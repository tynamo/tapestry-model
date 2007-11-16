/*
 * Created on 20/12/2005 by Eduardo Piva <eduardo@gwe.com.br>
 *
 */
package org.trails.page;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

public abstract class ExceptionPage extends TrailsPage implements IExternalPage
{

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
	{
		if (getCallbackStack() != null)
		{
			getCallbackStack().clear();
		}
	}
}
