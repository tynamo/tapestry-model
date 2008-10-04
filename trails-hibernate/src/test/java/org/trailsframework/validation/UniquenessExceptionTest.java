package org.trailsframework.validation;

import junit.framework.TestCase;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.testhibernate.Foo;

public class UniquenessExceptionTest extends TestCase
{

	public UniquenessExceptionTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public UniquenessExceptionTest(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public void testMessage()
	{
		TrailsPropertyDescriptor descriptor = new TrailsPropertyDescriptor(Foo.class, "Howdy", String.class);
		UniquenessException exception = new UniquenessException(descriptor);
		assertEquals("Howdy must be unique.", exception.getMessage());
	}
}
