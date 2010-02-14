package org.tynamo.hibernate.services;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.hibernate.HibernateTransactionDecorator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;
import org.hibernate.mapping.PersistentClass;
import org.tynamo.VersionedModule;
import org.tynamo.descriptor.DescriptorDecorator;
import org.tynamo.hibernate.TynamoHibernateSymbols;
import org.tynamo.hibernate.TynamoInterceptor;
import org.tynamo.hibernate.TynamoInterceptorConfigurer;
import org.tynamo.hibernate.validation.HibernateClassValidatorFactory;
import org.tynamo.hibernate.validation.HibernateValidationDelegate;

import java.util.Iterator;

public class TynamoHibernateModule extends VersionedModule
{

	public static void bind(ServiceBinder binder)
	{
		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(HibernatePersistenceService.class, HibernatePersistenceServiceImpl.class);
		binder.bind(HibernateClassValidatorFactory.class, HibernateClassValidatorFactory.class);
		binder.bind(HibernateValidationDelegate.class, HibernateValidationDelegate.class);
		binder.bind(TynamoInterceptor.class);
		binder.bind(HibernateConfigurer.class, TynamoInterceptorConfigurer.class).withId("TynamoInterceptorConfigurer");

	}

	/**
	 * Add our components and pages to the "trails" library.
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("tynamo-hibernate", "org.tynamo.hibernate"));
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration)
	{
		configuration.add("tynamo-hibernate/" + version, "org/tynamo/hibernate");
	}

	public static void contributeValidationMessagesSource(OrderedConfiguration<String> configuration)
	{
		configuration.add("Tynamo", "ValidationMessages");
	}

	@Match("HibernatePersistenceService")
	public static <T> T decorateTransactionally(HibernateTransactionDecorator decorator, Class<T> serviceInterface,
												T delegate,
												String serviceId)
	{
		return decorator.build(serviceInterface, delegate, serviceId);
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
	public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration)
	{

	}

	public static void contributeDescriptorFactory(OrderedConfiguration<DescriptorDecorator> configuration,
	                                               @Autobuild HibernateDescriptorDecorator hibernateDescriptorDecorator)
	{
		configuration.add("Hibernate", hibernateDescriptorDecorator);
	}

	@SuppressWarnings("unchecked")
	public static void contributeDescriptorService(Configuration<Class> configuration,
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


	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
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
	public static void contributeHibernateSessionSource(OrderedConfiguration<HibernateConfigurer> config,
														@InjectService("TynamoInterceptorConfigurer")
														HibernateConfigurer trailsInterceptorConfigurer)
	{
		config.add("TynamoInterceptorConfigurer", trailsInterceptorConfigurer);
	}


/**
 * We don't need this just yet, and it gives an error under Tapestry 5.1.0.2
 *
 * [INFO] [talledLocalContainer] java.lang.IllegalArgumentException:
 * Contribution org.tynamo.hibernate.services.TynamoHibernateModule.contributeTynamoEntityPackageManager(Configuration, HibernateEntityPackageManager)
 * (at TynamoHibernateModule.java:145) is for service 'TynamoEntityPackageManager', which does not exist.
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
