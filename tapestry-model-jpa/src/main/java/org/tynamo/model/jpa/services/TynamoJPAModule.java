package org.tynamo.model.jpa.services;

import com.kenai.tapestryjpa.JPAEntityManagerSource;
import com.kenai.tapestryjpa.JPATransactionDecorator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;
import org.tynamo.VersionedModule;
import org.tynamo.descriptor.DescriptorDecorator;
import org.tynamo.descriptor.DescriptorFactory;
import org.tynamo.descriptor.annotation.AnnotationDecorator;
import org.tynamo.model.jpa.TynamoJPASymbols;
import org.tynamo.util.Pair;

import javax.persistence.metamodel.ManagedType;
import java.util.Iterator;
import java.util.Set;

public class TynamoJPAModule extends VersionedModule {

	public static void bind(ServiceBinder binder) {
		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(JPAPersistenceService.class, JPAPersistenceServiceImpl.class);
		/* TODO validation
		binder.bind(HibernateClassValidatorFactory.class, HibernateClassValidatorFactory.class);
		binder.bind(HibernateValidationDelegate.class, HibernateValidationDelegate.class);
		*/
		//binder.bind(TynamoInterceptor.class);
		//binder.bind(JPAConfigurer.class, TynamoInterceptorConfigurer.class).withId("TynamoInterceptorConfigurer");

	}

	/** Add our components and pages to the "trails" library. */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("tynamo-jpa", "org.tynamo.model.jpa"));
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
		configuration.add("tynamo-jpa/" + version, "org/tynamo/jpa");
	}

	public static void contributeValidationMessagesSource(OrderedConfiguration<String> configuration) {
		configuration.add("Tynamo", "ValidationMessages");
	}

	@Match("JPAPersistenceService")
	public static <T> T decorateTransactionally(JPATransactionDecorator decorator, Class<T> serviceInterface,
												T delegate,
												String serviceId) {
		return decorator.build(serviceInterface, delegate, serviceId);
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
												   JPADescriptorDecorator JPADescriptorDecorator) {
		configuration.add("Hibernate", JPADescriptorDecorator);
		configuration.add("Annotation", new AnnotationDecorator());
	}

	@SuppressWarnings("unchecked")
	public static void contributeDescriptorService(Configuration<Class> configuration,
												   JPAEntityManagerSource entityManagerSource) {

		//org.hibernate.cfg.Configuration config = entityManagerSource.getConfiguration();
		Set<ManagedType<?>> mappings = entityManagerSource.getEntityManagerFactory().getMetamodel().getManagedTypes();
		Iterator<ManagedType<?>> iter = mappings.iterator();
		while (iter.hasNext()) {
			final ManagedType persistentClass = iter.next();
			final Class entityClass = persistentClass.getJavaType();

			if (entityClass != null) {
				configuration.add(entityClass);
			}
		}
	}

	public static void contributeTynamoDataTypeAnalyzer(OrderedConfiguration<Pair> configuration) {

		addPairToOrderedConfiguration(configuration, "hidden", "hidden");
		addPairToOrderedConfiguration(configuration, "readOnly", "readOnly");
		addPairToOrderedConfiguration(configuration, "richText", "fckEditor");
//		addPairToOrderedConfiguration(configuration, "name.toLowerCase().endsWith('password')", "passwordEditor"); //USE @DataType("password")
//		addPairToOrderedConfiguration(configuration, "string and !large and !identifier", "stringEditor"); //managed by Tapestry
		addPairToOrderedConfiguration(configuration, "string and large and !identifier", "longtext");
		addPairToOrderedConfiguration(configuration, "date", "dateEditor");
//		addPairToOrderedConfiguration(configuration, "numeric and !identifier", "numberEditor"); //managed by Tapestry
		addPairToOrderedConfiguration(configuration, "identifier && generated", "readOnly");
		addPairToOrderedConfiguration(configuration, "identifier && not(generated) && string", "identifierEditor");
//		addPairToOrderedConfiguration(configuration, "identifier && objectReference", "objectReferenceIdentifierEditor");
//		addPairToOrderedConfiguration(configuration, "boolean", "booleanEditor"); //managed by Tapestry
//		addPairToOrderedConfiguration(configuration, "supportsExtension('org.tynamo.descriptor.extension.EnumReferenceDescriptor')", "enumEditor"); //managed by Tapestry
		addPairToOrderedConfiguration(configuration, "supportsExtension('org.tynamo.descriptor.extension.BlobDescriptorExtension')", "blob");

		addPairToOrderedConfiguration(configuration, "objectReference", "single-valued-association" /* (aka: ManyToOne) */);
		addPairToOrderedConfiguration(configuration, "collection && not(childRelationship)", "many-valued-association" /* (aka: ManyToMany) */);
		addPairToOrderedConfiguration(configuration, "collection && childRelationship", "composition");
		addPairToOrderedConfiguration(configuration, "name == 'id'", "readOnly");
		addPairToOrderedConfiguration(configuration, "embedded", "embedded");
	}

	private static void addPairToOrderedConfiguration(OrderedConfiguration<Pair> configuration, String key, String value) {
		configuration.add(key, new Pair<String, String>(key, value));
	}


	public static void contributePropertyDescriptorFactory(Configuration<String> configuration) {
		configuration.add("exclude.*");
		configuration.add("class");
	}

	public static void contributeMethodDescriptorFactory(Configuration<String> configuration) {
		configuration.add("shouldExclude");
		configuration.add("set.*");
		configuration.add("get.*");
		configuration.add("is.*");
		configuration.add("equals");
		configuration.add("wait");
		configuration.add("toString");
		configuration.add("notify.*");
		configuration.add("hashCode");
	}

	public static JPADescriptorDecorator buildHibernateDescriptorDecorator(
			JPAEntityManagerSource hibernateSessionSource,
			DescriptorFactory descriptorFactory,
			@Symbol(TynamoJPASymbols.LARGE_COLUMN_LENGTH)
			final int largeColumnLength,
			@Symbol(TynamoJPASymbols.IGNORE_NON_HIBERNATE_TYPES)
			final boolean ignoreNonHibernateTypes) {
		return new JPADescriptorDecorator(hibernateSessionSource, descriptorFactory, largeColumnLength, ignoreNonHibernateTypes);
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
		configuration.add(TynamoJPASymbols.LARGE_COLUMN_LENGTH, "100");
		configuration.add(TynamoJPASymbols.IGNORE_NON_HIBERNATE_TYPES, "false");
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
