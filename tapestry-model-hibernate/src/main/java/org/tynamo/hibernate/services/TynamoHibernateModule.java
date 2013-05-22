package org.tynamo.hibernate.services;

import java.util.Iterator;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.BeanBlockSource;
import org.apache.tapestry5.services.ClasspathAssetAliasManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.messages.ComponentMessagesSource;
import org.hibernate.mapping.PersistentClass;
import org.tynamo.common.ModuleProperties;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.descriptor.factories.DescriptorFactory;
import org.tynamo.hibernate.TynamoHibernateSymbols;
import org.tynamo.hibernate.TynamoInterceptor;
import org.tynamo.hibernate.TynamoInterceptorConfigurer;
import org.tynamo.hibernate.decorators.HibernateDescriptorDecorator;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.TynamoCoreModule;

public class TynamoHibernateModule
{
	private static final String version = ModuleProperties.getVersion(TynamoCoreModule.class);

	public static void bind(ServiceBinder binder)
	{
		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(HibernatePersistenceService.class, HibernatePersistenceServiceImpl.class);
		binder.bind(TynamoInterceptor.class);
		binder.bind(HibernateConfigurer.class, TynamoInterceptorConfigurer.class).withId("TynamoInterceptorConfigurer");

	}

	/**
	 * Add our components and pages to the "tynamo" library.
	 */
	@Contribute(ComponentClassResolver.class)
	public static void componentClassResolver(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("tynamo", "org.tynamo.hibernate"));
	}

	@Contribute(ClasspathAssetAliasManager.class)
	public static void classpathAssetAliasManager(MappedConfiguration<String, String> configuration)
	{
		configuration.add("tynamo-hibernate-" + version, "org/tynamo/hibernate");
	}

	@Contribute(ComponentMessagesSource.class)
	public static void componentMessagesSource(OrderedConfiguration<String> configuration)
	{
		configuration.add("Tynamo", "ValidationMessages");
	}

	@Match("HibernatePersistenceService")
	public static void adviseTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver)
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
	public static void contributeDefaultDataTypeAnalyzer(MappedConfiguration<Class, String> configuration)
	{

	}

	/**
	 * Contribution to the BeanBlockSource service to tell the BeanEditForm component about the editors. When the
	 * BeanEditForm sees a property of type BigDecimal, it will map that to datatype "currency" and from there to the
	 * currency block of the AppPropertyEditBlocks page of the application.
	 */
	@Contribute(BeanBlockSource.class)
	public static void beanBlockSource(Configuration<BeanBlockContribution> configuration)
	{

	}

	@Contribute(DescriptorFactory.class)
	public static void descriptorFactory(OrderedConfiguration<DescriptorDecorator> configuration,
	                                               @Autobuild HibernateDescriptorDecorator hibernateDescriptorDecorator)
	{
		configuration.add("HibernateDescriptorDecorator", hibernateDescriptorDecorator, "after:TynamoDecorator");
	}

	@Contribute(DescriptorService.class)
	public static void descriptorService(Configuration<Class> configuration,
												   HibernateSessionSource hibernateSessionSource)
	{

		org.hibernate.cfg.Configuration config = hibernateSessionSource.getConfiguration();
		Iterator<PersistentClass> mappings = config.getClassMappings();
		while (mappings.hasNext())
		{
			final PersistentClass persistentClass = mappings.next();
			final Class entityClass = persistentClass.getMappedClass();

			if (entityClass != null)
			{
				configuration.add(entityClass);
			}
		}
	}

	@Contribute(SymbolProvider.class)
	@FactoryDefaults
	public static void setupFactoryDefaultsSymbols(MappedConfiguration<String, String> configuration)
	{
		configuration.add(TynamoHibernateSymbols.LARGE_COLUMN_LENGTH, "100");
		configuration.add(TynamoHibernateSymbols.IGNORE_NON_HIBERNATE_TYPES, "false");
	}

	/**
	 * Adds the following configurers:
	 * <dl>
	 * <dt>TynamoInterceptorConfigurer
	 * <dd>add the TynamoInterceptor to the hibernate configuration
	 */
	@Contribute(HibernateSessionSource.class)
	public static void hibernateSessionSource(OrderedConfiguration<HibernateConfigurer> config,
														@InjectService("TynamoInterceptorConfigurer")
														HibernateConfigurer interceptorConfigurer)
	{
		config.add("TynamoInterceptorConfigurer", interceptorConfigurer);
	}

/*
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
