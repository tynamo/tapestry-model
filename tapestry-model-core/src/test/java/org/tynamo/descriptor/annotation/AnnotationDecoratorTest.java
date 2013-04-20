package org.tynamo.descriptor.annotation;

import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.test.MockTester;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.tynamo.descriptor.*;
import org.tynamo.descriptor.annotation.handlers.ClassDescriptorAnnotationHandler;
import org.tynamo.descriptor.annotation.handlers.DescriptorAnnotationHandler;
import org.tynamo.descriptor.annotation.handlers.PropertyDescriptorAnnotationHandler;
import org.tynamo.descriptor.decorators.TynamoDecorator;
import org.tynamo.test.Embeddee;
import org.tynamo.test.Embeddor;
import org.tynamo.test.Foo;


public class AnnotationDecoratorTest extends Assert
{
	private final MockTester tester = new MockTester();

	/**
	 * Discards any mock objects created during the test.
	 */
	@AfterMethod(alwaysRun = true)
	public final void discardMockControl()
	{
		tester.cleanup();
	}

	@Test
	public void testDecorate()
	{
		ObjectLocator locator = tester.newMock(ObjectLocator.class);
		TynamoDecorator decorator = new TynamoDecorator(locator);

		EasyMock.expect(locator.getService("ClassDescriptorAnnotationHandler", DescriptorAnnotationHandler.class))
				.andReturn(new ClassDescriptorAnnotationHandler());

		EasyMock.expect(locator.getService("PropertyDescriptorAnnotationHandler", DescriptorAnnotationHandler.class))
				.andReturn(new PropertyDescriptorAnnotationHandler()).anyTimes();

		tester.replay();

		TynamoClassDescriptor descriptor = new TynamoClassDescriptorImpl(Annotated.class);
		TynamoPropertyDescriptor fieldPropDescriptor = new TynamoPropertyDescriptorImpl(Annotated.class, "notBloppity", String.class);

		TynamoPropertyDescriptor hiddenDescriptor = new TynamoPropertyDescriptorImpl(Annotated.class, "hidden", String.class);

		TynamoPropertyDescriptor booleanDescriptor = new TynamoPropertyDescriptorImpl(Annotated.class, "booleanProperty", boolean.class);

		descriptor.getPropertyDescriptors().add(fieldPropDescriptor);
		descriptor.getPropertyDescriptors().add(hiddenDescriptor);
		descriptor.getPropertyDescriptors().add(new IdentifierDescriptorImpl(Foo.class, "id", Integer.class));
		descriptor.getPropertyDescriptors().add(booleanDescriptor);

		descriptor = decorator.decorate(descriptor);

		assertTrue(descriptor.getPropertyDescriptor("notBloppity").isReadOnly());
		assertTrue(descriptor.getPropertyDescriptor("hidden").isNonVisual());
		assertTrue(descriptor.getPropertyDescriptor("id") instanceof IdentifierDescriptor, "still an id descriptor");

		tester.verify();
	}

	@Test
	public void testDecorateEmbedded() throws Exception
	{
		TynamoDecorator decorator = new TynamoDecorator(null);

		TynamoClassDescriptor embeddorDescriptor = new TynamoClassDescriptorImpl(Embeddor.class);

		EmbeddedDescriptor embeddeeDescriptor = new EmbeddedDescriptor(Embeddor.class,
				new TynamoPropertyDescriptorImpl(Embeddor.class, "embeddee", Embeddee.class),
				new TynamoClassDescriptorImpl(Embeddee.class));

		embeddeeDescriptor.getEmbeddedClassDescriptor().getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Embeddee.class, "title", String.class));
		embeddeeDescriptor.getEmbeddedClassDescriptor().getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Embeddee.class, "description", String.class));
		embeddorDescriptor.getPropertyDescriptors().add(embeddeeDescriptor);

		embeddorDescriptor = decorator.decorate(embeddorDescriptor);

		assertTrue(embeddorDescriptor.getPropertyDescriptors().get(0) instanceof EmbeddedDescriptor);
		embeddeeDescriptor = (EmbeddedDescriptor) embeddorDescriptor.getPropertyDescriptors().get(0);
		assertEquals(2, embeddeeDescriptor.getEmbeddedClassDescriptor().getPropertyDescriptors().size(), "2 props");
		assertEquals(embeddeeDescriptor.getEmbeddedClassDescriptor().getPropertyDescriptor("title").getName(), "title");
	}
}
