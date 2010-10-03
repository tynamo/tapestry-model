package org.tynamo.descriptor;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.test.Foo;

public class ObjectReferenceDescriptorTest extends Assert
{

	@Test
	public void testClone() throws Exception
	{
		ObjectReferenceDescriptor descriptor = new ObjectReferenceDescriptor(Foo.class, Foo.class, Foo.class);
		ObjectReferenceDescriptor descriptor2 = (ObjectReferenceDescriptor) descriptor.clone();
		assertEquals(Foo.class, descriptor2.getBeanType());
	}
}
