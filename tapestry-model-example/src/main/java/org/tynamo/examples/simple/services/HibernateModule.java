package org.tynamo.examples.simple.services;

import org.apache.tapestry5.hibernate.HibernateEntityPackageManager;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.modules.HibernateCoreModule;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.tynamo.hibernate.TynamoHibernateSymbols;
import org.tynamo.hibernate.modules.TynamoHibernate4SearchModule;
import org.tynamo.hibernate.services.TynamoHibernateModule;

@SubModule(value = {
		TynamoHibernateModule.class,
		TynamoHibernate4SearchModule.class,
		org.apache.tapestry5.hibernate.modules.HibernateModule.class,
		HibernateCoreModule.class
})
public class HibernateModule
{

	@Contribute(SymbolProvider.class)
	@ApplicationDefaults
	public static void provideSymbols(MappedConfiguration<String, String> configuration)
	{
		configuration.add(TynamoHibernateSymbols.IGNORE_NON_HIBERNATE_TYPES, "true");
	}

	/**
	 * By default tapestry-hibernate will scan
	 * InternalConstants.TAPESTRY_APP_PACKAGE_PARAM + ".entities" (witch is equal to "org.tynamo.examples.simple.simple.entities")
	 * for annotated entity classes.
	 * <p/>
	 * Contributes the package "org.tynamo.examples.simple.simple.model" to the configuration, so that it will be
	 * scanned for annotated entity classes.
	 */
	@Contribute(HibernateEntityPackageManager.class)
	public static void addPackagesToScan(Configuration<String> configuration)
	{
//		If you want to scan other packages add them here:
//		configuration.add("org.tynamo.examples.simple.simple.model");
	}

	/**
	 * Adds the CommitAfter annotation work, to process the
	 * {@link org.apache.tapestry5.hibernate.annotations.CommitAfter} annotation.
	 */
	@Contribute(ComponentClassTransformWorker2.class)
	@Primary
	public static void provideCommitAfterAnnotationSupport(OrderedConfiguration<ComponentClassTransformWorker2> configuration,
	                                                       final HibernateSessionManager manager)
	{
		MethodAdvice advice = new MethodAdvice()
		{
			public void advise(MethodInvocation invocation)
			{
				try
				{
					invocation.proceed();
					manager.commit();
				} catch (RuntimeException ex)
				{
					try
					{
						manager.abort();
					} catch (Exception e)
					{
					}
					throw ex;
				}
			}
		};
		configuration.add("TynamoExampleCommitAfter", new CommitAfterWorker(advice), "after:Log");
	}

}
