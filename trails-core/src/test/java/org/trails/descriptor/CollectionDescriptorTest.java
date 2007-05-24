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
		IPropertyDescriptor collectionPropertyDescriptor = new TrailsPropertyDescriptor(Foo.class, "bings", Set.class);

		//CollectionDescriptor bingsDescriptor = new CollectionDescriptor(Foo.class, "bings", Set.class);
		CollectionDescriptor bingsDescriptor = new CollectionDescriptor(Foo.class, collectionPropertyDescriptor);
		collectionPropertyDescriptor.addExtension(CollectionDescriptor.class.getName(), bingsDescriptor);
		bingsDescriptor.setElementType(Bing.class);
		//assertEquals("addBing", bingsDescriptor.findAddExpression());
		assertEquals("bings.add", bingsDescriptor.findAddExpression());
		//assertEquals("removeBing", bingsDescriptor.findRemoveExpression());
		assertEquals("bings.remove", bingsDescriptor.findRemoveExpression());
		collectionPropertyDescriptor = new TrailsPropertyDescriptor(Foo.class, "bazzes", Set.class);
		CollectionDescriptor bazzesDescriptor = new CollectionDescriptor(Foo.class, collectionPropertyDescriptor);
		collectionPropertyDescriptor.addExtension(CollectionDescriptor.class.getName(), bazzesDescriptor);
		bazzesDescriptor.setElementType(Baz.class);
		assertEquals("bazzes.add", bazzesDescriptor.findAddExpression());
	}

	public void testClone() {
		IPropertyDescriptor collectionPropertyDescriptor = new TrailsPropertyDescriptor(Foo.class, "bazzes", Set.class);
		CollectionDescriptor bazzesDescriptor = new CollectionDescriptor(Foo.class, collectionPropertyDescriptor);
		collectionPropertyDescriptor.addExtension(CollectionDescriptor.class.getName(), bazzesDescriptor);
		bazzesDescriptor.setElementType(Baz.class);
		bazzesDescriptor.setOneToMany(true);
		bazzesDescriptor.setChildRelationship(true);
		bazzesDescriptor.setInverseProperty("inverse");
		bazzesDescriptor.getPropertyDescriptor().setName(null);

		CollectionDescriptor clonedDescriptor = (CollectionDescriptor) bazzesDescriptor.clone();

		assertTrue(clonedDescriptor.isChildRelationship());
		assertEquals("inverse", clonedDescriptor.getInverseProperty());
		assertTrue(clonedDescriptor.isOneToMany());
		assertNull(clonedDescriptor.getPropertyDescriptor().getName());
	}
}
