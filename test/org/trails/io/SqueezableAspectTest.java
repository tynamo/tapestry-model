package org.trails.io;

import junit.framework.TestCase;

import org.trails.test.Foo;

public class SqueezableAspectTest extends TestCase
{
	public void testThatEntitiesAreSqueezable()
	{
		Foo foo = new Foo();
		assertTrue(foo instanceof Squeezable);
	}
}
