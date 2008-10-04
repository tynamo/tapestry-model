package org.trails.callback;

import java.util.Stack;

import org.apache.tapestry.callback.ICallback;

public class CallbackStack extends Stack<ICallback>
{

	/**
	 * @return the callback of the previous page, assumes the current page will
	 *         be at the top of the stack
	 */
	public ICallback popPreviousCallback()
	{
		if (size() > 1)
		{
			pop();
			return pop();
		} else
		{
			clear(); //make sure the stack is empty; 
			return null;
		}


	}

	public ICallback getPreviousCallback()
	{
		if (size() > 1)
		{
			return get(size() - 2);
		} else return null;
	}
}