package org.trails.descriptor;

import junit.framework.TestCase;

import org.trails.test.Foo;

public class ObjectReferenceDescriptorTest extends TestCase
{

	public ObjectReferenceDescriptorTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ObjectReferenceDescriptorTest(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public void testClone() throws Exception
	{
		IPropertyDescriptor orDescriptor = new TrailsPropertyDescriptor(Foo.class, Foo.class);
		ObjectReferenceDescriptor descriptor = new ObjectReferenceDescriptor(Foo.class, orDescriptor);
		ObjectReferenceDescriptor descriptor2 = (ObjectReferenceDescriptor)descriptor.clone();
		assertEquals(Foo.class, descriptor2.getPropertyType());
	}
}
