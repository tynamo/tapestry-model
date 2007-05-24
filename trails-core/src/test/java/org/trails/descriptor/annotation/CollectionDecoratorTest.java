package org.trails.descriptor.annotation;

import junit.framework.TestCase;

import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;

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
		IPropertyDescriptor collectionPropertyDescriptor = new TrailsPropertyDescriptor(Annotated.class, "stuff", Annotated.class);

		CollectionDescriptor collectionDescriptor = new CollectionDescriptor(Annotated.class, collectionPropertyDescriptor, Annotated.class);
		collectionPropertyDescriptor.addExtension(collectionDescriptor.getClass().getName(), collectionDescriptor);
		collectionDescriptor.setOneToMany(true);
		Collection collectionAnnotation = Annotated.class.getDeclaredMethod("getStuff").getAnnotation(Collection.class);
		collectionPropertyDescriptor = decorator.decorateFromAnnotation(collectionAnnotation, collectionPropertyDescriptor);
		assertTrue("is child", collectionPropertyDescriptor.getExtension(CollectionDescriptor.class).isChildRelationship());
		assertEquals("Stuff is inversed by 'annotated'", "annotated", collectionPropertyDescriptor.getExtension(CollectionDescriptor.class).getInverseProperty());
		assertTrue(collectionDescriptor.isOneToMany());
	}
}
