package org.trails.callback;

import junit.framework.TestCase;
import org.trails.test.Foo;

public class CallbackStackTest extends TestCase
{

	public void testPush()
	{
		CallbackStack callbackStack = new CallbackStack();
		Foo foo = new Foo();
		Foo foo2 = new Foo();
		EditCallback editCallback = new EditCallback("FooEdit", foo);
		EditCallback editCallback2 = new EditCallback("FooEdit", foo2);
		callbackStack.push(editCallback);
		callbackStack.push(editCallback2);
		assertEquals(
			2, callbackStack.getStack().size());

		callbackStack.getStack().clear();
		editCallback.setModelNew(true);
		callbackStack.push(editCallback);
		callbackStack.push(editCallback2);
		assertEquals(
			1, callbackStack.getStack().size());
	}


	public void testPreviousCallback()
	{
		CallbackStack callbackStack = new CallbackStack();
		EditCallback callback = new EditCallback("FooEdit", new Foo());
		ListCallback listCallback = new ListCallback("ListEdit", Foo.class);
		callbackStack.push(callback);
		callbackStack.push(listCallback);
		assertEquals(callback, callbackStack.getPreviousCallback());
		assertEquals(2, callbackStack.getStack().size());
		assertEquals(callback, callbackStack.popPreviousCallback());
		assertTrue("stack is empty", callbackStack.getStack().isEmpty());
	}
}
