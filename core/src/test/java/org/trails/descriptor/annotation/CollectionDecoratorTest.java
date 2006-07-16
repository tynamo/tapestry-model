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
        CollectionDecorator decorator = new CollectionDecorator();
        CollectionDescriptor collectionDescriptor = new CollectionDescriptor(Foo.class, "stuff", Annotated.class);
        Collection collectionAnnotation = Annotated.class.getDeclaredMethod("getStuff").getAnnotation(Collection.class);
        assertTrue("is child", decorator.decorateFromAnnotation(
                collectionAnnotation, collectionDescriptor).isChildRelationship());
    }   
}
