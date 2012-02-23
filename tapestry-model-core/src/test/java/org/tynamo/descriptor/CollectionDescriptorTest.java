package org.tynamo.descriptor;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.test.Baz;
import org.tynamo.test.Bing;
import org.tynamo.test.Foo;

public class CollectionDescriptorTest extends Assert
{
	@Test
	public void testClone()
	{
		CollectionDescriptor bazzesDescriptor = new CollectionDescriptor(Foo.class, "bazzes", Set.class);
		bazzesDescriptor.setElementType(Baz.class);
		bazzesDescriptor.setOneToMany(true);
		bazzesDescriptor.setChildRelationship(true);
		bazzesDescriptor.setInverseProperty("inverse");

		CollectionDescriptor clonedDescriptor = (CollectionDescriptor) bazzesDescriptor.clone();

		assertTrue(clonedDescriptor.isChildRelationship());
		assertEquals("inverse", clonedDescriptor.getInverseProperty());
		assertTrue(clonedDescriptor.isOneToMany());
	}
}
