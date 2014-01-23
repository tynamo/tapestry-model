package org.tynamo.model.jpa;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.internal.test.PageTesterContext;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.jpa.modules.JpaModule;
import org.apache.tapestry5.modules.TapestryModule;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.EmbeddedDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptorImpl;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptorImpl;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.model.jpa.services.JpaDescriptorDecorator;
import org.tynamo.model.test.entities.Bar;
import org.tynamo.model.test.entities.Baz;
import org.tynamo.model.test.entities.Descendant;
import org.tynamo.model.test.entities.Embeddee;
import org.tynamo.model.test.entities.Embeddor;
import org.tynamo.model.test.entities.Foo;
import org.tynamo.model.test.entities.IBar;
import org.tynamo.services.TynamoCoreModule;


public class JpaDescriptorDecoratorTest
{

	DescriptorDecorator jpaDescriptorDecorator;
	TynamoClassDescriptor classDescriptor;

	private static Registry registry;

	@BeforeSuite
	public final void setup_registry()
	{
		RegistryBuilder builder = new RegistryBuilder();
		builder.add(TapestryModule.class);
		builder.add(JpaModule.class);
		builder.add(TynamoCoreModule.class);
		builder.add(TestModule.class);

		registry = builder.build();
    ApplicationGlobals globals = registry.getObject(ApplicationGlobals.class, null);
    globals.storeContext(new PageTesterContext(""));

		registry.performRegistryStartup();

	}

	@AfterSuite
	public final void shutdown_registry()
	{
		registry.shutdown();

		registry = null;
	}

	@AfterMethod
	public final void cleanupThread()
	{
		// registry.cleanupThread();
	}

	@BeforeMethod
	public void setUp()
 {
		jpaDescriptorDecorator = registry.getService(JpaDescriptorDecorator.class);


		TynamoClassDescriptor fooDescriptor = new TynamoClassDescriptorImpl(Foo.class);
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "bazzes", Set.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "bings", Set.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "id", Integer.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "name", String.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "hidden", String.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "date", Date.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "readOnly", String.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "multiWordProperty", String.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "primitive", boolean.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "bar", IBar.class));
		fooDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "fromFormula", String.class));

		classDescriptor = jpaDescriptorDecorator.decorate(fooDescriptor);
	}

	@Test
	public void testNameDescriptor() throws Exception
	{
		TynamoPropertyDescriptor nameDescriptor = classDescriptor.getPropertyDescriptor("name");
		Assert.assertEquals(nameDescriptor.getName(), "name", "is name");
	}

	@Test
	public void testIdDescriptor() throws Exception
	{
		IdentifierDescriptor idDescriptor = (IdentifierDescriptor) classDescriptor.getIdentifierDescriptor();
		Assert.assertTrue(idDescriptor.isIdentifier(), "is id");
		Assert.assertFalse(idDescriptor.isGenerated(), "not generated");
	}

// JPA2 doesn't have support for @Formula, also see comments in Foo.getFromFormula()
//	@Test
//	public void testFormulaDescriptor() throws Exception
//	{
//		TynamoPropertyDescriptor formulaDescriptor = classDescriptor.getPropertyDescriptor("fromFormula");
//		Assert.assertTrue(formulaDescriptor.isReadOnly());
//	}

	@Test
	public void testCollectionDescriptor() throws Exception
	{

		CollectionDescriptor bazzesDescriptor = (CollectionDescriptor) classDescriptor.getPropertyDescriptor("bazzes");
		Assert.assertTrue(bazzesDescriptor.isCollection(), "bazzes is a collection");
		Assert.assertEquals(bazzesDescriptor.getElementType(), Baz.class, "right element type");
		Assert.assertTrue(bazzesDescriptor.isChildRelationship(), "bazzes are children");
		Assert.assertTrue(bazzesDescriptor.isOneToMany());
		Assert.assertEquals(bazzesDescriptor.getInverseProperty(), "foo", "bazzes are mapped by 'foo' property in Baz");

//      @note: HibernateDescriptorDecorator doesn't set hasCyclicRelationships anymore but it might do it again if we
//      switch from Descriptors to Extensions
//		Assert.assertTrue(classDescriptor.getHasCyclicRelationships(), "Foo has a cyclic relationship");
	}

	@Test
	public void testGetClassDescriptors() throws Exception
	{

		Assert.assertFalse(classDescriptor.isChild(), "not a child");
		List<TynamoPropertyDescriptor> propertyDescriptors = classDescriptor.getPropertyDescriptors();
		Assert.assertEquals(propertyDescriptors.size(), 11, "got 11");

		class NameFilter implements Predicate<TynamoPropertyDescriptor>
		{
			private String nameToFilter;

			NameFilter(String nameToFilter)
			{
				this.nameToFilter = nameToFilter;
			}

			@Override
			public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
			{
				return nameToFilter.equals(tynamoPropertyDescriptor.getName());
			}
		}

		TynamoPropertyDescriptor barDescriptor = F.flow(propertyDescriptors).filter(new NameFilter("bar")).toList().get(0);

		Assert.assertEquals(barDescriptor.getName(), "bar", "name");
		Assert.assertTrue(!barDescriptor.isIdentifier(), "is not an id");

		TynamoPropertyDescriptor hiddenDescriptor = F.flow(propertyDescriptors).filter(new NameFilter("hidden")).toList().get(0);
		Assert.assertNotNull(hiddenDescriptor, "didn't blow up");

		TynamoPropertyDescriptor primitiveDescriptor = F.flow(propertyDescriptors).filter(new NameFilter("primitive")).toList().get(0);
		Assert.assertTrue(primitiveDescriptor.isBoolean(), "is boolean");

	}

	@Test
	public void testIsObjectReference() throws Exception
	{
		TynamoPropertyDescriptor propertyDescriptor = classDescriptor.getPropertyDescriptor("bar");
		Assert.assertTrue(propertyDescriptor.isObjectReference());
		Assert.assertEquals(propertyDescriptor.getPropertyType(), Bar.class, "got right class");

		TynamoPropertyDescriptor primitiveDescriptor = classDescriptor.getPropertyDescriptor("primitive");
		Assert.assertFalse(primitiveDescriptor.isObjectReference());
	}

	@Test
	public void testLengthLarge() throws Exception
	{
		TynamoPropertyDescriptor multiWordDescriptor = classDescriptor.getPropertyDescriptor("multiWordProperty");
		Assert.assertTrue(multiWordDescriptor.isLarge());
		Assert.assertEquals(multiWordDescriptor.getLength(), 101, "right length");
		TynamoPropertyDescriptor nameDescriptor = classDescriptor.getPropertyDescriptor("name");
		Assert.assertFalse(nameDescriptor.isLarge(), "not large");
	}

	@Test
	public void testInheritance() throws Exception
	{
		TynamoClassDescriptor descendantDescriptor = new TynamoClassDescriptorImpl(Descendant.class);
		descendantDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "bazzes", Set.class));
		descendantDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "extra", String.class));
		descendantDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "id", Integer.class));
		descendantDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Foo.class, "name", String.class));
		TynamoClassDescriptor decorated = jpaDescriptorDecorator.decorate(descendantDescriptor);
		Assert.assertEquals(4, decorated.getPropertyDescriptors().size());
	}

	@Test
	public void testEmbedded() throws Exception
	{
		TynamoClassDescriptor embeddorDescriptor = new TynamoClassDescriptorImpl(Embeddor.class);
		embeddorDescriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Embeddor.class, "embeddee", Embeddee.class));
		TynamoClassDescriptor decorated = jpaDescriptorDecorator.decorate(embeddorDescriptor);
		TynamoPropertyDescriptor propertyDescriptor = decorated.getPropertyDescriptors().get(0);
		Assert.assertTrue(propertyDescriptor.isEmbedded());
		EmbeddedDescriptor embeddedDescriptor = (EmbeddedDescriptor) propertyDescriptor;
		Assert.assertEquals("embeddee", embeddedDescriptor.getName());
		Assert.assertEquals(Embeddor.class, embeddedDescriptor.getBeanType(), "right bean type");
		Assert.assertEquals(3, embeddedDescriptor.getEmbeddedClassDescriptor().getPropertyDescriptors().size(), "3 prop descriptors");
	}

	@Test
	public void testTransient() throws Exception
	{
		TynamoClassDescriptor descriptor = new TynamoClassDescriptorImpl(Bar.class);
		descriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Bar.class, "name", String.class));
		descriptor.getPropertyDescriptors().add(new TynamoPropertyDescriptorImpl(Bar.class, "transientProperty", String.class));
		TynamoClassDescriptor decorated = jpaDescriptorDecorator.decorate(descriptor);
		Assert.assertFalse(decorated.getPropertyDescriptor("transientProperty").isSearchable());
		Assert.assertTrue(decorated.getPropertyDescriptor("name").isSearchable());
	}
}
