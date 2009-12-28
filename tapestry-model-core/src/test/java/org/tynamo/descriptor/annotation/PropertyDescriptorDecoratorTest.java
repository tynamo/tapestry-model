package org.tynamo.descriptor.annotation;

import java.util.Date;

import junit.framework.TestCase;
import org.tynamo.descriptor.IPropertyDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;
import org.tynamo.test.Foo;

public class PropertyDescriptorDecoratorTest extends TestCase
{
	public void testPropertyDescriptorDecorator() throws Exception
	{
		PropertyDescriptorAnnotationHandler decorator = new PropertyDescriptorAnnotationHandler();
		TrailsClassDescriptor descriptor = new TrailsClassDescriptor(Annotated.class);
		IPropertyDescriptor fieldPropDescriptor = new TrailsPropertyDescriptor(Foo.class, "notBloppity", String.class);
		IPropertyDescriptor hiddenDescriptor = new TrailsPropertyDescriptor(Foo.class, "Hidden", String.class);
		IPropertyDescriptor dateDescriptor = new TrailsPropertyDescriptor(Foo.class, "date", Date.class);
		descriptor.getPropertyDescriptors().add(fieldPropDescriptor);
		descriptor.getPropertyDescriptors().add(hiddenDescriptor);

		PropertyDescriptor fieldAppearance = Annotated.class.getDeclaredField("notBloppity").getAnnotation(PropertyDescriptor.class);

		fieldPropDescriptor = decorator.decorateFromAnnotation(fieldAppearance, fieldPropDescriptor);
		assertEquals("Bloppity", fieldPropDescriptor.getDisplayName());
		assertEquals("right index", 2, (fieldPropDescriptor).getIndex());

		PropertyDescriptor propDescriptorAnno = Annotated.class.getMethod("getHidden").getAnnotation(PropertyDescriptor.class);
		IPropertyDescriptor hiddenPropDescriptor = decorator.decorateFromAnnotation(propDescriptorAnno, hiddenDescriptor);
		assertTrue(hiddenPropDescriptor.isHidden());
		assertTrue(hiddenPropDescriptor.getIndex() != 2);
		assertEquals("Hidden", hiddenPropDescriptor.getDisplayName());

		PropertyDescriptor dateDescriptorAnno = Annotated.class.getMethod("getDate").getAnnotation(PropertyDescriptor.class);
		IPropertyDescriptor datePropDescriptor = decorator.decorateFromAnnotation(dateDescriptorAnno, dateDescriptor);
		assertEquals("MM/dd/yyyy", datePropDescriptor.getFormat());
	}
}
