package org.trails.test;

import org.trails.demo.Thing;
import org.trails.hibernate.HasAssignedIdentifier;

import junit.framework.TestCase;

public class ThingTest extends TestCase
{
	public void testThing()
	{
		Thing thing = new Thing();
		assertTrue(thing instanceof HasAssignedIdentifier);
	}
}
