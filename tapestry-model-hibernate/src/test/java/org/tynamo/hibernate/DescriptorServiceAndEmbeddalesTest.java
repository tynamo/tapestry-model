package org.tynamo.hibernate;

import org.apache.tapestry5.hibernate.HibernateCoreModule;
import org.apache.tapestry5.hibernate.HibernateModule;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.services.TapestryModule;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.tynamo.descriptor.EmbeddedDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.hibernate.modules.TynamoHibernate4Module;
import org.tynamo.hibernate.services.TynamoHibernateModule;
import org.tynamo.model.test.entities.Embeddee;
import org.tynamo.model.test.entities.Embeddor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.TynamoCoreModule;

/**
 * https://jira.codehaus.org/browse/TYNAMO-22
 *
 * Properties in embeddable (@Embeddable) components are not being identified by the TynamoDataTypeAnalyzer, because there
 * is no classDescriptor for Embeddables in the descriptorService.
 *
 */
public class DescriptorServiceAndEmbeddalesTest
{
	private DescriptorService descriptorService;

	private static Registry registry;

	@SuppressWarnings("unchecked")
	@BeforeSuite
	public final void setup_registry()
	{
		RegistryBuilder builder = new RegistryBuilder();
		builder.add(TapestryModule.class);
		builder.add(HibernateCoreModule.class);
		builder.add(HibernateModule.class);
		builder.add(TynamoCoreModule.class);
		builder.add(TynamoHibernateModule.class);
		builder.add(TynamoHibernate4Module.class);
		builder.add(TestModule.class);

		registry = builder.build();
		registry.performRegistryStartup();

		descriptorService = registry.getService(DescriptorService.class);

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
		registry.cleanupThread();
	}

	@Test
	public void there_should_be_classDescriptor_for_embedable_in_descriptorService() throws Exception
	{
		TynamoClassDescriptor embeddorDescriptor = descriptorService.getClassDescriptor(Embeddor.class);

		TynamoPropertyDescriptor embeddeePropertyDescriptor = embeddorDescriptor.getPropertyDescriptor("embeddee");

		Assert.assertTrue(embeddeePropertyDescriptor.isEmbedded());

		EmbeddedDescriptor embeddedDescriptor = (EmbeddedDescriptor) embeddeePropertyDescriptor;

		Assert.assertEquals("embeddee", embeddedDescriptor.getName());
		Assert.assertEquals(Embeddor.class, embeddedDescriptor.getBeanType(), "right bean type");
		Assert.assertEquals(embeddedDescriptor.getEmbeddedClassDescriptor().getPropertyDescriptors().size(), 3, "3 prop descriptors");

		Assert.assertNotNull(descriptorService.getClassDescriptor(Embeddee.class));
	}

}
