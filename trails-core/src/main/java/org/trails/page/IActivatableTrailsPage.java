package org.trails.page;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.engine.IEngineService;
import org.trails.callback.CallbackStack;


public interface IActivatableTrailsPage extends IPage, SimpleTrailsBasePage
{

	void activateTrailsPage(Object[] objects, IRequestCycle iRequestCycle);

	void pushCallback();

	/**
	 * This property is injected with the callbackStack ASO
	 *
	 * @return
	 */
	public abstract CallbackStack getCallbackStack();

	@InjectObject(value = "service:trails.core.TrailsPagesService")
	public abstract IEngineService getTrailsPagesService();


}
