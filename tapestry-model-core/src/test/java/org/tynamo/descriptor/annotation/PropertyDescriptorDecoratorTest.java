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

		decorator.decorateFromAnnotation(fieldAppearance, fieldPropDescriptor);
		assertEquals("notBloppity", fieldPropDescriptor.getName());
		assertTrue(fieldPropDescriptor.isReadOnly());

		PropertyDescriptor propDescriptorAnno = Annotated.class.getMethod("getHidden").getAnnotation(PropertyDescriptor.class);
		decorator.decorateFromAnnotation(propDescriptorAnno, hiddenDescriptor);
		assertTrue(hiddenDescriptor.isNonVisual());
		assertEquals("Hidden", hiddenDescriptor.getName());

		PropertyDescriptor dateDescriptorAnno = Annotated.class.getMethod("getDate").getAnnotation(PropertyDescriptor.class);
		decorator.decorateFromAnnotation(dateDescriptorAnno, dateDescriptor);
		assertEquals("MM/dd/yyyy", dateDescriptor.getFormat());
	}
}
