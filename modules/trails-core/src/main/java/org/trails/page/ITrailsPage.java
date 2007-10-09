package org.trails.page;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.Persist;
import org.trails.descriptor.IClassDescriptor;
import org.trails.callback.CallbackStack;


public interface ITrailsPage extends IPage
{

	IClassDescriptor getClassDescriptor();

	void setClassDescriptor(IClassDescriptor iClassDescriptor);

	/**
	 * This property is injected with the callbackStack ASO
	 *
	 * @return
	 */
	CallbackStack getCallbackStack();
}
