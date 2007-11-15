package org.trails.callback;

import junit.framework.TestCase;

public class CallbackStackTest extends TestCase
{

	public void testPush()
	{
		CallbackStack callbackStack = new CallbackStack();

		UrlCallback callback = new UrlCallback("http://localhost:8080/trails/view/Foo/1");
		UrlCallback callback2 = new UrlCallback("http://localhost:8080/trails/list/Foo");

		callbackStack.push(callback);
		callbackStack.push(callback2);
		assertEquals(2, callbackStack.size());

		UrlCallback callback3 = new UrlCallback("http://localhost:8080/trails/view/Foo/1");
		callbackStack.push(callback3);

		// the callback stack shouldn't check for equals
		assertEquals(3, callbackStack.size());

		callbackStack.clear();

		callbackStack.push(callback);
		callbackStack.push(callback2);
		assertEquals(2, callbackStack.size());
	}


	public void testPreviousCallback()
	{
		CallbackStack callbackStack = new CallbackStack();
		UrlCallback callback = new UrlCallback("http://localhost:8080/trails/view/Foo/1");
		UrlCallback callback2 = new UrlCallback("http://localhost:8080/trails/list/Foo");

		callbackStack.push(callback);
		callbackStack.push(callback2);

		assertEquals(callback, callbackStack.getPreviousCallback());
		assertEquals(2, callbackStack.size());
		assertEquals(callback, callbackStack.popPreviousCallback());
		assertTrue("stack is empty", callbackStack.isEmpty());
	}
}
