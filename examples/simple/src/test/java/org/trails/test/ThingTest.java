package org.trails.test;

import junit.framework.TestCase;
import org.trails.demo.Thing;
import org.trails.hibernate.HasAssignedIdentifier;

public class ThingTest extends TestCase
{
	public void testThing()
	{
		Thing thing = new Thing();
		assertTrue(thing instanceof HasAssignedIdentifier);
	}
}
