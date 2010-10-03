package org.tynamo.descriptor.annotation;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.descriptor.*;
import org.tynamo.descriptor.decorators.TynamoDecorator;
import org.tynamo.test.Embeddee;
import org.tynamo.test.Embeddor;
import org.tynamo.test.Foo;


public class AnnotationDecoratorTest extends Assert
{
	TynamoDecorator decorator = new TynamoDecorator();

	@Test
	public void testDecorate()
	{

		TynamoClassDescriptor descriptor = new TynamoClassDescriptorImpl(Annotated.class);
		TynamoPropertyDescriptor fieldPropDescriptor = new TynamoPropertyDescriptorImpl(Annotated.class, "notBloppity", String.class);

		TynamoPropertyDescriptor hiddenDescriptor = new TynamoPropertyDescriptorImpl(Annotated.class, "hidden", String.class);
		hiddenDescriptor.setIndex(1);

		TynamoPropertyDescriptor validatedStringDescriptor = new TynamoPropertyDescriptorImpl(Foo.class, "validatedString", String.class);
		validatedStringDescriptor.setIndex(3);

		TynamoPropertyDescriptor booleanDescriptor = new TynamoPropertyDescriptorImpl(Annotated.class, "booleanProperty", boolean.class);

		descriptor.getPropertyDescriptors().add(fieldPropDescriptor);
		descriptor.getPropertyDescriptors().add(hiddenDescriptor);
		descriptor.getPropertyDescriptors().add(validatedStringDescriptor);
		descriptor.getPropertyDescriptors().add(new IdentifierDescriptorImpl(Foo.class, "id", Integer.class));
		descriptor.getPropertyDescriptors().add(booleanDescriptor);

		descriptor = decorator.decorate(descriptor);

		assertEquals("notBloppity", descriptor.getPropertyDescriptors().get(2).getName(), "right index");
		assertTrue(descriptor.getPropertyDescriptor("hidden").isNonVisual());
		assertTrue(descriptor.getPropertyDescriptor("id") instanceof IdentifierDescriptor, "still an id descriptor");
	}

	@Test
	public void testDecorateEmbedded() throws Exception
	{
		TynamoClassDescriptor embeddorDescriptor = new TynamoClassDescriptorImpl(Embeddor.class);
		EmbeddedDescriptor embeddeeDescriptor = new EmbeddedDescriptor(Embeddor.class, "embeddee", Embeddee.class);
		embeddeeDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Embeddee.class, "title", String.class));
		embeddeeDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Embeddee.class, "description", String.class));
		embeddorDescriptor.getPropertyDescriptors().add(embeddeeDescriptor);

		embeddorDescriptor = decorator.decorate(embeddorDescriptor);

		assertTrue(embeddorDescriptor.getPropertyDescriptors().get(0) instanceof EmbeddedDescriptor);
		embeddeeDescriptor = (EmbeddedDescriptor) embeddorDescriptor.getPropertyDescriptors().get(0);
		assertEquals(2, embeddeeDescriptor.getPropertyDescriptors().size(), "2 props");
		assertEquals(embeddeeDescriptor.getPropertyDescriptor("title").getName(), "title");
	}
}
