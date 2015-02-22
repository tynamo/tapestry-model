package org.tynamo.model.jpa.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.apache.tapestry5.jpa.JpaTransactionAdvisor;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.BeanBlockSource;
import org.apache.tapestry5.services.LibraryMapping;
import org.tynamo.common.ModuleProperties;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.descriptor.factories.DescriptorFactory;
import org.tynamo.model.jpa.TynamoJpaSymbols;
import org.tynamo.model.jpa.internal.ConfigurableEntityManagerProvider;
import org.tynamo.model.jpa.internal.SearchableJpaGridDataSourceProvider;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.SearchableGridDataSourceProvider;
import org.tynamo.services.TynamoCoreModule;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

public class TynamoJpaModule {

	private static final String version = ModuleProperties.getVersion(TynamoCoreModule.class);

	public static void bind(ServiceBinder binder) {

		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(JpaPersistenceService.class, JpaPersistenceServiceImpl.class);
		binder.bind(ConfigurableEntityManagerProvider.class);
	}

	/** Add our components and pages to the "tynamo-jpa" library. */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("tynamo", "org.tynamo.model.jpa"));
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
		configuration.add("tynamo-jpa-" + version, "org/tynamo/model/jpa");
	}

// FIXME validationMessageSources doesn't exist when running tests
//	public static void contributeValidationMessagesSource(OrderedConfiguration<String> configuration) {
//		configuration.add("Tynamo", "ValidationMessages");
//	}

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
	@Contribute(BeanBlockSource.class)
	public static void beanBlockSource(Configuration<BeanBlockContribution> configuration) {

	}

	@Contribute(DescriptorFactory.class)
	public static void descriptorFactory(OrderedConfiguration<DescriptorDecorator> configuration,
												   @Autobuild JpaDescriptorDecorator jpaDescriptorDecorator) {
		// jpaDescriptorDecorator is responsible for creating the idDescriptor. We have to locate the id first so that
		// search extensions would work properly. Are there any properties that jpaDescriptorDecorator sets but
		// TynamoDecorator or other decorators would reset later?
		configuration.add("JPA", jpaDescriptorDecorator, "before:TynamoDecorator");
	}

	@SuppressWarnings("rawtypes")
	@Contribute(DescriptorService.class)
	public static void descriptorService(Configuration<Class> configuration,
	                                     ConfigurableEntityManagerProvider entityManagerProvider) {

		EntityManager entityManager = entityManagerProvider.getEntityManager();
		for (EntityType<?> mapping : entityManager.getMetamodel().getEntities()) configuration.add(mapping.getJavaType());
//		for (EmbeddableType<?> mapping : entityManager.getMetamodel().getEmbeddables()) configuration.add(mapping.getJavaType());
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.add(TynamoJpaSymbols.LARGE_COLUMN_LENGTH, 100);
		configuration.add(TynamoJpaSymbols.IGNORE_NON_JPA_TYPES, false);
		configuration.add(TynamoJpaSymbols.PERSISTENCEUNIT, "");
	}

	@Contribute(ServiceOverride.class)
	public static void setupApplicationServiceOverrides(MappedConfiguration<Class, Object> configuration, ObjectLocator locator) {
		configuration.add(SearchableGridDataSourceProvider.class,
				new SearchableJpaGridDataSourceProvider(locator.getService(ConfigurableEntityManagerProvider.class)));
	}

	@Match("JpaPersistenceService")
	public static void adviseTransactions(JpaTransactionAdvisor advisor, MethodAdviceReceiver receiver)
	{
		advisor.addTransactionCommitAdvice(receiver);
	}
}
