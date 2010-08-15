package org.tynamo.descriptor.annotation;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptorImpl;
import org.tynamo.descriptor.TynamoPropertyDescriptorImpl;
import org.tynamo.descriptor.annotation.handlers.PropertyDescriptorAnnotationHandler;
import org.tynamo.test.Foo;

public class PropertyDescriptorDecoratorTest extends Assert
{
	@Test
	public void testPropertyDescriptorDecorator() throws Exception
	{
		PropertyDescriptorAnnotationHandler decorator = new PropertyDescriptorAnnotationHandler();
		TynamoClassDescriptorImpl descriptor = new TynamoClassDescriptorImpl(Annotated.class);
		TynamoPropertyDescriptor fieldPropDescriptor = new TynamoPropertyDescriptorImpl(Foo.class, "notBloppity", String.class);
		TynamoPropertyDescriptor hiddenDescriptor = new TynamoPropertyDescriptorImpl(Foo.class, "Hidden", String.class);
		TynamoPropertyDescriptor dateDescriptor = new TynamoPropertyDescriptorImpl(Foo.class, "date", Date.class);
		descriptor.getPropertyDescriptors().add(fieldPropDescriptor);
		descriptor.getPropertyDescriptors().add(hiddenDescriptor);

		PropertyDescriptor fieldAppearance = Annotated.class.getDeclaredField("notBloppity").getAnnotation(PropertyDescriptor.class);

		fieldPropDescriptor = decorator.decorateFromAnnotation(fieldAppearance, fieldPropDescriptor);
		assertEquals("notBloppity", fieldPropDescriptor.getName());
		assertEquals(2, (fieldPropDescriptor).getIndex(), "right index");

		PropertyDescriptor propDescriptorAnno = Annotated.class.getMethod("getHidden").getAnnotation(PropertyDescriptor.class);
		TynamoPropertyDescriptor hiddenPropDescriptor = decorator.decorateFromAnnotation(propDescriptorAnno, hiddenDescriptor);
		assertTrue(hiddenPropDescriptor.isHidden());
		assertTrue(hiddenPropDescriptor.getIndex() != 2);
		assertEquals("Hidden", hiddenPropDescriptor.getName());

		PropertyDescriptor dateDescriptorAnno = Annotated.class.getMethod("getDate").getAnnotation(PropertyDescriptor.class);
		TynamoPropertyDescriptor datePropDescriptor = decorator.decorateFromAnnotation(dateDescriptorAnno, dateDescriptor);
		assertEquals("MM/dd/yyyy", datePropDescriptor.getFormat());
	}
}
