package org.tynamo.descriptor;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.test.Embeddee;
import org.tynamo.test.Embeddor;
import org.tynamo.test.Foo;

public class EmbeddedDescriptorTest extends Assert
{
	EmbeddedDescriptor embeddedDescriptor = new EmbeddedDescriptor(Embeddor.class, "embeddee", Embeddee.class);

	@Test
	public void testClone() throws Exception
	{
		EmbeddedDescriptor embeddedDescriptor = new EmbeddedDescriptor(Embeddor.class, "embeddee", Embeddee.class);
		embeddedDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Embeddee.class, "title", String.class));
		EmbeddedDescriptor cloned = (EmbeddedDescriptor) embeddedDescriptor.clone();
		assertEquals(Embeddor.class, cloned.getBeanType());
		assertEquals(Embeddee.class, cloned.getType());
		assertEquals(1, cloned.getPropertyDescriptors().size());
	}

	@Test public void testCopyFromPropertyDescriptor() throws Exception
	{
		TynamoPropertyDescriptor propertyDescriptor = new TynamoPropertyDescriptorImpl(Foo.class, "blork", String.class);
		propertyDescriptor.setIndex(1);
		embeddedDescriptor.copyFrom(propertyDescriptor);
		assertEquals("blork", embeddedDescriptor.getName());
		assertEquals(1, embeddedDescriptor.getIndex());
	}
}
