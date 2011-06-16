package org.tynamo.descriptor.annotation;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.descriptor.annotation.handlers.CollectionDescriptorAnnotationHandler;
import org.tynamo.test.Foo;

public class CollectionDescriptorDecoratorTest extends Assert
{

	@Test
	public void testDecorator() throws Exception
	{
		CollectionDescriptorAnnotationHandler decorator = new CollectionDescriptorAnnotationHandler();
		org.tynamo.descriptor.CollectionDescriptor collectionDescriptor = new org.tynamo.descriptor.CollectionDescriptor(Foo.class, "stuff", Annotated.class);
		collectionDescriptor.setOneToMany(true);
		CollectionDescriptor collectionAnnotation = Annotated.class.getDeclaredMethod("getStuff").getAnnotation(CollectionDescriptor.class);
		decorator.decorateFromAnnotation(collectionAnnotation, collectionDescriptor);
		assertTrue(collectionDescriptor.isChildRelationship(), "is child");
		assertEquals("annotated", collectionDescriptor.getInverseProperty(), "Stuff is inversed by 'annotated'");
		assertTrue(collectionDescriptor.isOneToMany());
	}
}
