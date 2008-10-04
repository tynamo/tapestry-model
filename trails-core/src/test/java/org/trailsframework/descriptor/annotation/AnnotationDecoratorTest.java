package org.trails.descriptor.annotation;


import junit.framework.TestCase;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.descriptor.EmbeddedDescriptor;
import org.trails.test.Embeddee;
import org.trails.test.Embeddor;
import org.trails.test.Foo;

public class AnnotationDecoratorTest extends TestCase
{
	AnnotationDecorator decorator = new AnnotationDecorator();

	public void testDecorate()
	{


		IClassDescriptor descriptor = new TrailsClassDescriptor(Annotated.class, "Annotated");
		IPropertyDescriptor fieldPropDescriptor = new TrailsPropertyDescriptor(Annotated.class, "notBloppity", String.class);

		descriptor.getPropertyDescriptors().add(fieldPropDescriptor);
		IPropertyDescriptor hiddenDescriptor = new TrailsPropertyDescriptor(Annotated.class, "hidden", String.class);
		hiddenDescriptor.setIndex(1);
		descriptor.getPropertyDescriptors().add(hiddenDescriptor);
		IPropertyDescriptor validatedStringDescriptor = new TrailsPropertyDescriptor(Foo.class, "validatedString", String.class);
		validatedStringDescriptor.setIndex(3);

		IPropertyDescriptor booleanDescriptor = new TrailsPropertyDescriptor(Annotated.class, "booleanProperty", boolean.class);
		descriptor.getPropertyDescriptors().add(validatedStringDescriptor);
		descriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Foo.class, "id", Integer.class));
		descriptor.getPropertyDescriptors().add(booleanDescriptor);

		descriptor = decorator.decorate(descriptor);
		assertEquals(Annotated.CLASS_LABEL, descriptor.getDisplayName());
		assertEquals("right index", "notBloppity",
			((IPropertyDescriptor) descriptor.getPropertyDescriptors().get(2)).getName());
		assertTrue(descriptor.getPropertyDescriptor("hidden").isHidden());
		assertEquals(Annotated.NOT_BLOPPITY_LABEL,
			descriptor.getPropertyDescriptor("notBloppity").getDisplayName());
		assertTrue("still an id descriptor",
			descriptor.getPropertyDescriptor("id") instanceof IdentifierDescriptor);
		validatedStringDescriptor = descriptor.getPropertyDescriptor("validatedString");

		assertEquals(Annotated.BOOLEAN_LABEL,
			descriptor.getPropertyDescriptor("booleanProperty").getDisplayName());

	}

	public void testDecorateEmbedded() throws Exception
	{
		IClassDescriptor embeddorDescriptor = new TrailsClassDescriptor(Embeddor.class, "Embeddor");
		EmbeddedDescriptor embeddeeDescriptor = new EmbeddedDescriptor(Embeddor.class, "embeddee", Embeddee.class);
		embeddeeDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Embeddee.class, "title", String.class));
		embeddeeDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Embeddee.class, "description", String.class));
		embeddorDescriptor.getPropertyDescriptors().add(embeddeeDescriptor);
		embeddorDescriptor = decorator.decorate(embeddorDescriptor);
		assertTrue(embeddorDescriptor.getPropertyDescriptors().get(0) instanceof EmbeddedDescriptor);
		embeddeeDescriptor = (EmbeddedDescriptor) embeddorDescriptor.getPropertyDescriptors().get(0);
		assertEquals("2 props", 2, embeddeeDescriptor.getPropertyDescriptors().size());
		assertEquals("The Title", embeddeeDescriptor.getPropertyDescriptor("title").getDisplayName());
	}
}
