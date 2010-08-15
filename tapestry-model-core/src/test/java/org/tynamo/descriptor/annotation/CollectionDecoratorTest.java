package org.tynamo.descriptor.annotation;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.annotation.handlers.CollectionDescriptorAnnotationHandler;
import org.tynamo.test.Foo;

public class CollectionDecoratorTest extends Assert
{

	@Test
	public void testDecorator() throws Exception
	{
		CollectionDescriptorAnnotationHandler decorator = new CollectionDescriptorAnnotationHandler();
		CollectionDescriptor collectionDescriptor = new CollectionDescriptor(Foo.class, "stuff", Annotated.class);
		collectionDescriptor.setOneToMany(true);
		Collection collectionAnnotation = Annotated.class.getDeclaredMethod("getStuff").getAnnotation(Collection.class);
		collectionDescriptor = decorator.decorateFromAnnotation(collectionAnnotation, collectionDescriptor);
		assertTrue(collectionDescriptor.isChildRelationship(), "is child");
		assertEquals("annotated", collectionDescriptor.getInverseProperty(), "Stuff is inversed by 'annotated'");
		assertTrue(collectionDescriptor.isOneToMany());
	}
}
