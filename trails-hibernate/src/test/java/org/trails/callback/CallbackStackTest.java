package org.trails.callback;

import org.hibernate.criterion.DetachedCriteria;
import org.jmock.MockObjectTestCase;
import org.trails.test.Foo;

public class CallbackStackTest extends MockObjectTestCase
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
		Assert.assertEquals(
			2, callbackStack.getStack().size());

		callbackStack.getStack().clear();
		editCallback.setModelNew(true);
		callbackStack.push(editCallback);
		callbackStack.push(editCallback2);
		Assert.assertEquals(
			1, callbackStack.getStack().size());
	}


	public void testPreviousCallback()
	{
		CallbackStack callbackStack = new CallbackStack();
		EditCallback callback = new EditCallback("FooEdit", new Foo());
		ListCallback listCallback = new ListCallback("ListEdit", Foo.class.getName(), DetachedCriteria.forClass(Foo.class));
		callbackStack.push(callback);
		callbackStack.push(listCallback);
		Assert.assertEquals(callback, callbackStack.getPreviousCallback());
		Assert.assertEquals(2, callbackStack.getStack().size());
		Assert.assertEquals(callback, callbackStack.popPreviousCallback());
		Assert.assertTrue("stack is empty", callbackStack.getStack().isEmpty());
	}
}