package org.trails.descriptor.annotation;

import junit.framework.TestCase;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.test.Foo;

public class CollectionDecoratorTest extends TestCase
{

	public CollectionDecoratorTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public CollectionDecoratorTest(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public void testDecorator() throws Exception
	{
		CollectionDescriptorAnnotationHandler decorator = new CollectionDescriptorAnnotationHandler();
		CollectionDescriptor collectionDescriptor = new CollectionDescriptor(Foo.class, "stuff", Annotated.class);
		collectionDescriptor.setOneToMany(true);
		Collection collectionAnnotation = Annotated.class.getDeclaredMethod("getStuff").getAnnotation(Collection.class);
		collectionDescriptor = decorator.decorateFromAnnotation(collectionAnnotation, collectionDescriptor);
		assertTrue("is child", collectionDescriptor.isChildRelationship());
		assertEquals("Stuff is inversed by 'annotated'", "annotated", collectionDescriptor.getInverseProperty());
		assertTrue(collectionDescriptor.isOneToMany());
	}
}
