package org.trails.callback;

import java.util.Stack;

public class CallbackStack
{
	
	private Stack<TrailsCallback> stack = new Stack<TrailsCallback>();

	public Stack<TrailsCallback> getStack()
	{
		return stack;
	}

	public void setStack(Stack<TrailsCallback> stack)
	{
		this.stack = stack;
	}
	
	/**
	 * If this callback should replace the previous callback,
	 * pop it off before we push this one on.
	 * @param callback
	 */
	public void push(TrailsCallback callback)
	{
		if (!getStack().empty() && (callback.shouldReplace(getStack().peek())))
		{
			getStack().pop();
		}
		getStack().push(callback);
	}
	
	

	/**
	 * 
	 * @return the callback of the previous page, assumes the current page will
	 * be at the top of the stack
	 */
	public TrailsCallback popPreviousCallback()
	{
		if (getStack().size() > 1)
		{
			getStack().pop();
			return getStack().pop();
		}
		else return null;
	}

	public TrailsCallback getPreviousCallback()
	{
		if (getStack().size() > 1)
		{
			return getStack().get(getStack().size() - 2);
		}
		else return null;
	}
}
