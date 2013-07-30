package org.tynamo.model.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.apache.tapestry5.jpa.JpaTransactionAdvisor;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.BeanBlockSource;
import org.apache.tapestry5.services.LibraryMapping;
import org.elasticsearch.common.network.NetworkUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.tynamo.common.ModuleProperties;
import org.tynamo.descriptor.annotation.handlers.DescriptorAnnotationHandler;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.descriptor.factories.DescriptorFactory;
import org.tynamo.model.elasticsearch.annotations.handlers.ElasticSearchAnnotationHandler;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;
import org.tynamo.model.elasticsearch.mapping.impl.DefaultMapperFactory;
import org.tynamo.model.jpa.TynamoJpaSymbols;
import org.tynamo.model.jpa.internal.ConfigurableEntityManagerProvider;
import org.tynamo.model.jpa.internal.ElasticSearchIndexMaintainer;
import org.tynamo.model.jpa.internal.SearchableJpaGridDataSourceProvider;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.SearchableGridDataSourceProvider;
import org.tynamo.services.TynamoCoreModule;

public class TynamoJpaModule {
	private static final String version = ModuleProperties.getVersion(TynamoCoreModule.class);

	public static void bind(ServiceBinder binder) {

		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(JpaPersistenceService.class, JpaPersistenceServiceImpl.class);
		binder.bind(DescriptorAnnotationHandler.class, ElasticSearchAnnotationHandler.class).withId(
			"ElasticSearchAnnotationHandler");

		binder.bind(MapperFactory.class, DefaultMapperFactory.class);
		//binder.bind(TynamoInterceptor.class);
		//binder.bind(JPAConfigurer.class, TynamoInterceptorConfigurer.class).withId("TynamoInterceptorConfigurer");

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
		@Autobuild ConfigurableEntityManagerProvider entityManagerProvider) {

		EntityManager entityManager = entityManagerProvider.getEntityManager();
		for (EntityType<?> mapping : entityManager.getMetamodel().getEntities()) configuration.add(mapping.getJavaType());
//		for (EmbeddableType<?> mapping : entityManager.getMetamodel().getEmbeddables()) configuration.add(mapping.getJavaType());
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
		configuration.add(TynamoJpaSymbols.LARGE_COLUMN_LENGTH, "100");
		configuration.add(TynamoJpaSymbols.IGNORE_NON_HIBERNATE_TYPES, "false");
		configuration.add(TynamoJpaSymbols.PERSISTENCEUNIT, "");
		configuration.add(TynamoJpaSymbols.ELASTICSEARCH_HOME, "");
		configuration.add(TynamoJpaSymbols.ELASTICSEARCH_HTTP_ENABLED, "false");
	}

	@Contribute(ServiceOverride.class)
	public static void setupApplicationServiceOverrides(MappedConfiguration<Class, Object> configuration,
		@Autobuild SearchableJpaGridDataSourceProvider gridDataSourceProvider) {
		configuration.add(SearchableGridDataSourceProvider.class, gridDataSourceProvider);
	}

	public Node buildNode(@Symbol(TynamoJpaSymbols.ELASTICSEARCH_HOME) String pathHome,
	                      @Symbol(TynamoJpaSymbols.ELASTICSEARCH_HTTP_ENABLED) boolean httpEnabled,
	                      RegistryShutdownHub registryShutdownHub) {
		ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
		if (!pathHome.isEmpty()) settings.put("path.home", pathHome);
		settings.put("http.enabled", httpEnabled);
		settings.put("number_of_shards", 1);
		settings.put("number_of_replicas", 0);
		settings.put("cluster.name", "tynamo-model-search-" + NetworkUtils.getLocalAddress().getHostName()).build();
		final Node node = NodeBuilder.nodeBuilder().local(true).data(true).settings(settings).build();
		node.start();

		registryShutdownHub.addRegistryShutdownListener(new Runnable() {
			@Override
			public void run() {
				node.close(); // TYNAMO-223
			}
		});

		return node;
	}

	@Startup
	public static void addJpaEventListener(@Autobuild ElasticSearchIndexMaintainer indexMaintainer) {
		indexMaintainer.start();
	}

	@Match("JpaPersistenceService")
	public static void adviseTransactions(JpaTransactionAdvisor advisor, MethodAdviceReceiver receiver)
	{
		advisor.addTransactionCommitAdvice(receiver);
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
