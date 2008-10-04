package org.trails.page;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.IRequestCycle;

/**
 * A page which has a model object.
 *
 * @author Chris Nelson
 */
public abstract class ModelPage extends TrailsPage implements IModelPage
{

	public void pushCallback()
	{
		ICallback callback = callbackToThisPage();
		if (getCallbackStack() != null && (getCallbackStack().isEmpty() || !getCallbackStack().peek().equals(callback)))
		{
			getCallbackStack().push(callback);
		}
	}

	protected abstract ICallback callbackToThisPage();

	public void pageBeginRender(PageEvent event)
	{
		super.pageBeginRender(event);
		Defense.notNull(getModel(), "model");
		Defense.notNull(isModelNew(), "modelNew");
	}

}
