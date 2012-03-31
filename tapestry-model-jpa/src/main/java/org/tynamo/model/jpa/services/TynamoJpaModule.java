package org.tynamo.model.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.jpa.JpaTransactionAdvisor;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;
import org.tynamo.VersionedModule;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.model.jpa.TynamoJpaSymbols;
import org.tynamo.model.jpa.internal.ConfigurableEntityManagerProvider;

public class TynamoJpaModule extends VersionedModule {

	public static void bind(ServiceBinder binder) {

		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(JpaPersistenceService.class, JpaPersistenceServiceImpl.class);
		//binder.bind(TynamoInterceptor.class);
		//binder.bind(JPAConfigurer.class, TynamoInterceptorConfigurer.class).withId("TynamoInterceptorConfigurer");

	}

	/** Add our components and pages to the "tynamo-jpa" library. */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("tynamo-jpa", "org.tynamo.model.jpa"));
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
		configuration.add("tynamo-jpa-" + version, "org/tynamo/jpa");
	}

// FIXME validationMessageSources doesn't exist when running tests
//	public static void contributeValidationMessagesSource(OrderedConfiguration<String> configuration) {
//		configuration.add("Tynamo", "ValidationMessages");
//	}

	@Match("JPAPersistenceService")
	public static void adviseTransactions(JpaTransactionAdvisor advisor, MethodAdviceReceiver receiver)
	{
		advisor.addTransactionCommitAdvice(receiver);
	}

	/**
	 * Contributions to the DefaultDataTypeAnalyzer.
	 * <p/>
	 * DataTypeAnalyzer is a chain of command that can make match properties to data types based on property type or
	 * annotations on the property. In general, DefaultDataTypeAnalyzer is used, as that only needs to consider property
	 * type. DefaultDataTypeAnalyzer matches property types to data types, based on a search up the inheritance path.
	 */
	public static void contributeDefaultDataTypeAnalyzer(MappedConfiguration<Class, String> configuration) {

	}

	/**
	 * Contribution to the BeanBlockSource service to tell the BeanEditForm component about the editors. When the
	 * BeanEditForm sees a property of type BigDecimal, it will map that to datatype "currency" and from there to the
	 * currency block of the AppPropertyEditBlocks page of the application.
	 */
	public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {

	}

	public static void contributeDescriptorFactory(OrderedConfiguration<DescriptorDecorator> configuration,
												   @Autobuild JpaDescriptorDecorator jpaDescriptorDecorator) {
		configuration.add("JPA", jpaDescriptorDecorator);
	}

	@SuppressWarnings("rawtypes")
	public static void contributeDescriptorService(Configuration<Class> configuration, 
		@Autobuild ConfigurableEntityManagerProvider entityManagerProvider) {
		
		EntityManager entityManager = entityManagerProvider.getEntityManager();
		for (EntityType<?> mapping : entityManager.getMetamodel().getEntities()) {
			final Class entityClass = mapping.getJavaType();
			if (entityClass != null) configuration.add(entityClass);
		}
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
		configuration.add(TynamoJpaSymbols.LARGE_COLUMN_LENGTH, "100");
		configuration.add(TynamoJpaSymbols.IGNORE_NON_HIBERNATE_TYPES, "false");
		configuration.add(TynamoJpaSymbols.PERSISTENCEUNIT, "");
	}

	/**
	 * TODO: needed?
	 * Adds the following configurers:
	 * <dl>
	 * <dt>TynamoInterceptorConfigurer
	 * <dd>add the TynamoInterceptor to the jpa configuration

	 public static void contributeHibernateSessionSource(OrderedConfiguration<JPAConfigurer> config,
	 @InjectService("TynamoInterceptorConfigurer")
	 JPAConfigurer trailsInterceptorConfigurer)
	 {
	 //config.add("TynamoInterceptorConfigurer", trailsInterceptorConfigurer);
	 }
	 */

/**
 * We don't need this just yet, and it gives an error under Tapestry 5.1.0.2
 *
 * [INFO] [talledLocalContainer] java.lang.IllegalArgumentException:
 * Contribution org.tynamo.model.jpa.services.TynamoJPAModule.contributeTynamoEntityPackageManager(Configuration, HibernateEntityPackageManager)
 * (at TynamoJPAModule.java:145) is for service 'TynamoEntityPackageManager', which does not exist.
 *
 public static void contributeTynamoEntityPackageManager(Configuration<String> configuration, HibernateEntityPackageManager packageManager)
 {
 for (String packageName : packageManager.getPackageNames())
 {
 configuration.add(packageName);
 }
 }
 */


/*
	public static void contributeFieldValidatorSource(MappedConfiguration<String, Validator> configuration) {
			configuration.add("int", new ValidateInt());
	}
*/

}
