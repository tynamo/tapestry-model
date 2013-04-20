package org.tynamo.descriptor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tynamo.test.Bar;
import org.tynamo.test.BlogEntry;
import org.tynamo.test.Foo;
import org.tynamo.test.Searchee;


public class TynamoClassDescriptorTest extends Assert
{
	TynamoClassDescriptorImpl classDescriptor;
	IdentifierDescriptor idProp;
	TynamoPropertyDescriptor multiWordProp;

	@BeforeMethod
	public void setUp() throws Exception
	{
		classDescriptor = new TynamoClassDescriptorImpl(Foo.class);
		idProp = new IdentifierDescriptorImpl(Foo.class, "id", String.class);

		multiWordProp = new TynamoPropertyDescriptorImpl(Foo.class, "multiWordProperty", String.class);
		classDescriptor.getPropertyDescriptors().add(idProp);
		classDescriptor.getPropertyDescriptors().add(multiWordProp);
		classDescriptor.getMethodDescriptors().add(new TynamoMethodDescriptorImpl(Foo.class, "foo", void.class, new Class[]{}));
		classDescriptor.setHasCyclicRelationships(true);
	}

	@Test
	public void testClone() throws Exception
	{
		TynamoClassDescriptorImpl clone = (TynamoClassDescriptorImpl) classDescriptor.clone();
		assertEquals(Foo.class, clone.getBeanType(), "still foo");
		assertEquals(clone.getPropertyDescriptors().size(), 2, "2 props");
		assertTrue(clone.getPropertyDescriptor("id") instanceof IdentifierDescriptor, "clone has id");
		assertEquals(idProp.getName(), clone.getIdentifierDescriptor().getName(), "clone has id");
		assertEquals(1, clone.getMethodDescriptors().size(), "still has a method");
		assertTrue(clone.getHasCyclicRelationships(), "still has cyclic relationships");
	}

	@Test public void testCopyConstructor() throws Exception
	{
		TynamoClassDescriptorImpl copiedDescriptor = new TynamoClassDescriptorImpl(classDescriptor);
		assertEquals(copiedDescriptor.getBeanType().getSimpleName(), "Foo");
		assertEquals(2, copiedDescriptor.getPropertyDescriptors().size(), "2 properties");
		assertTrue(copiedDescriptor.getHasCyclicRelationships(), "still has cyclic relationships");
	}

	@Test public void testGetIdentifierProperty() throws Exception
	{
		assertEquals(idProp, classDescriptor.getIdentifierDescriptor(), "right id prop");

		EmbeddedDescriptor embeddeeDescriptor = new EmbeddedDescriptor(Foo.class,
				new TynamoPropertyDescriptorImpl(Foo.class, "blork", Bar.class),
				new TynamoClassDescriptorImpl(Bar.class));

		classDescriptor.getPropertyDescriptors().add(embeddeeDescriptor);

		assertEquals(idProp, classDescriptor.getIdentifierDescriptor(), "right id prop");

	}

	@Test public void testGetDescriptor() throws Exception
	{
		assertEquals(multiWordProp, classDescriptor.getPropertyDescriptor("multiWordProperty"), "got right descriptor");
		assertNull(classDescriptor.getPropertyDescriptor("doesntexist"), "should return null if none found");
		List descriptors = classDescriptor.getPropertyDescriptors(Arrays.asList("multiWordProperty", "id"));
		assertEquals(2, descriptors.size(), "get 2 descriptors");
		assertEquals(multiWordProp, descriptors.get(0), "in specified order");
	}

	@Test public void testHasCyclicRelationshipsDefaultValueFalse() throws Exception
	{
		classDescriptor = new TynamoClassDescriptorImpl(BlogEntry.class);
		assertFalse(classDescriptor.getHasCyclicRelationships(), "default value should be false");
	}

	@Test public void testGetSearchableProperties()
	{
		TynamoClassDescriptorImpl classDescriptor = new TynamoClassDescriptorImpl(Searchee.class);
		classDescriptor.getPropertyDescriptors().add(
				new TynamoPropertyDescriptorImpl(Foo.class, "someProperty", String.class));
		classDescriptor.getPropertyDescriptors().add(new IdentifierDescriptorImpl(Foo.class, "id", String.class));
		classDescriptor.getPropertyDescriptors().add(new CollectionDescriptor(Foo.class, "name", Set.class));

		List<TynamoPropertyDescriptor> searchableProperties = classDescriptor.getPropertyDescriptors();
		searchableProperties = F.flow(searchableProperties).filter(new Predicate<TynamoPropertyDescriptor>()
		{
			public boolean accept(TynamoPropertyDescriptor element)
			{
				return element.isSearchable();
			}
		}).toList();

		assertEquals(2, searchableProperties.size(), "should only be 2 search properties");
		assertEquals(searchableProperties.get(0).getName(), "someProperty");
	}
}
