package org.tynamo.examples.simple.services;

import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.jpa.JpaEntityPackageManager;
import org.tynamo.model.jpa.services.TynamoJpaModule;

@SubModule(value = {
		TynamoJpaModule.class,
		org.apache.tapestry5.jpa.modules.JpaModule.class
})
public class JpaModule
{

	/**
	 * By default tapestry-jpa will scan
	 * InternalConstants.TAPESTRY_APP_PACKAGE_PARAM + ".entities" (witch is equal to "org.tynamo.examples.simple.simple.entities")
	 * for annotated entity classes.
	 * <p/>
	 * Contributes the package "org.tynamo.examples.simple.simple.model" to the configuration, so that it will be
	 * scanned for annotated entity classes.
	 */
	@Contribute(JpaEntityPackageManager.class)
	public static void addPackagesToScan(Configuration<String> configuration)
	{
//		If you want to scan other packages add them here:
//		configuration.add("org.tynamo.examples.simple.simple.model");
	}

	// @Contribute(ComponentClassTransformWorker2.class)
	// @Primary
	// public static void provideClassTransformWorkers(OrderedConfiguration<ComponentClassTransformWorker2> configuration,
	// EntityManagerManager manager)
	// {
	// /**
	// * WARN: This JPACommitAfter worker doesn't support @PersistenceContext
	// */
//		MethodAdvice advice = new CommitAfterMethodAdvice(manager);
//		configuration.add("TynamoExampleJPACommitAfter", new CommitAfterWorker(advice), "after:Log");
	// }
}
