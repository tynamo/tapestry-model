package org.trails.io;

import org.trails.test.Foo;

import junit.framework.TestCase;

public class SqueezableAspectTest extends TestCase
{
	public void testThatEntitiesAreSqueezable()
	{
		Foo foo = new Foo();
		assertTrue(foo instanceof Squeezable);
	}
}
