package org.trails.descriptor;

import java.util.Set;

import org.trails.test.Baz;
import org.trails.test.Bing;
import org.trails.test.Foo;

import junit.framework.TestCase;

public class CollectionDescriptorTest extends TestCase
{
    public void testFindExpression()
    {
        CollectionDescriptor bingsDescriptor = new CollectionDescriptor(Foo.class, "bings", Set.class);
        bingsDescriptor.setElementType(Bing.class);
        assertEquals("addBing", bingsDescriptor.findAddExpression());
        assertEquals("removeBing", bingsDescriptor.findRemoveExpression());
        CollectionDescriptor bazzesDescriptor = new CollectionDescriptor(Foo.class, "bazzes", Set.class);
        bazzesDescriptor.setElementType(Baz.class);
        assertEquals("bazzes.add", bazzesDescriptor.findAddExpression());
    }
}
