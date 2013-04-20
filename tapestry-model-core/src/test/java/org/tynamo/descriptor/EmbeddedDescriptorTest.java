package org.tynamo.descriptor;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.test.Embeddee;
import org.tynamo.test.Embeddor;
import org.tynamo.test.Foo;

public class EmbeddedDescriptorTest extends Assert
{

	@Test
	public void testClone() throws Exception
	{
		EmbeddedDescriptor embeddedDescriptor = new EmbeddedDescriptor(Embeddor.class,
				new TynamoPropertyDescriptorImpl(Embeddor.class, "embeddee", Embeddee.class),
				new TynamoClassDescriptorImpl(Embeddee.class));

		embeddedDescriptor.getEmbeddedClassDescriptor().getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Embeddee.class, "title", String.class));
		EmbeddedDescriptor cloned = (EmbeddedDescriptor) embeddedDescriptor.clone();
		assertEquals(Embeddor.class, cloned.getBeanType());
		assertEquals(Embeddee.class, cloned.getPropertyType());
		assertEquals(1, cloned.getEmbeddedClassDescriptor().getPropertyDescriptors().size());
	}

	@Test public void testCopyFromPropertyDescriptor() throws Exception
	{
		EmbeddedDescriptor embeddedDescriptor = new EmbeddedDescriptor(Embeddor.class,
				new TynamoPropertyDescriptorImpl(Embeddor.class, "embeddee", Embeddee.class),
				new TynamoClassDescriptorImpl(Embeddee.class));

		TynamoPropertyDescriptor propertyDescriptor = new TynamoPropertyDescriptorImpl(Foo.class, "blork", String.class);
		embeddedDescriptor.copyFrom(propertyDescriptor);
		assertEquals("blork", embeddedDescriptor.getName());
	}
}
