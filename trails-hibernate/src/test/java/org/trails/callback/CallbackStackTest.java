package org.trails.callback;

import org.hibernate.criterion.DetachedCriteria;
import org.jmock.MockObjectTestCase;
import org.trails.testhibernate.Foo;

public class CallbackStackTest extends MockObjectTestCase
{

	public void testPreviousCallback()
	{
		CallbackStack callbackStack = new CallbackStack();
		EditCallback callback = new EditCallback("FooEdit", new Foo());
		HibernateListCallback listCallback = new HibernateListCallback("ListEdit", Foo.class.getName(), Foo.class, DetachedCriteria.forClass(Foo.class));
		callbackStack.push(callback);
		callbackStack.push(listCallback);
		assertEquals(callback, callbackStack.getPreviousCallback());
		assertEquals(2, callbackStack.getStack().size());
		assertEquals(callback, callbackStack.popPreviousCallback());
		assertTrue("stack is empty", callbackStack.getStack().isEmpty());
	}
}