package org.trails.callback;

import java.util.Stack;

import org.apache.tapestry.callback.ICallback;

public class CallbackStack
{

	private Stack<ICallback> stack = new Stack<ICallback>();

	public Stack<ICallback> getStack()
	{
		return stack;
	}

	public void setStack(Stack<ICallback> stack)
	{
		this.stack = stack;
	}

	/**
	 * If this callback should replace the previous callback,
	 * pop it off before we push this one on.
	 *
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
	 * If this callback is equals to the previous callback,
	 * pop it off before we push this one on.
	 *
	 * @param callback
	 */
	public void push(ICallback callback)
	{
		if (!getStack().empty() && (callback.equals(getStack().peek())))
		{
			getStack().pop();
		}
		getStack().push(callback);
	}

	/**
	 * @return the callback of the previous page, assumes the current page will
	 *         be at the top of the stack
	 */
	public ICallback popPreviousCallback()
	{
		if (getStack().size() > 1)
		{
			getStack().pop();
			return getStack().pop();
		} else return null;
	}

	public ICallback getPreviousCallback()
	{
		if (getStack().size() > 1)
		{
			return getStack().get(getStack().size() - 2);
		} else return null;
	}
}
