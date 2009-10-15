package org.tynamo.hibernate.services;

import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.hibernate.HibernateTransactionDecorator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;
import org.hibernate.EntityMode;
import org.hibernate.metadata.ClassMetadata;
import org.tynamo.VersionedModule;
import org.tynamo.descriptor.DescriptorDecorator;
import org.tynamo.descriptor.DescriptorFactory;
import org.tynamo.descriptor.annotation.AnnotationDecorator;
import org.tynamo.hibernate.TynamoHibernateSymbols;
import org.tynamo.hibernate.validation.HibernateClassValidatorFactory;
import org.tynamo.hibernate.validation.HibernateValidationDelegate;

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
		binder.bind(SeedEntity.class, SeedEntityImpl.class);

	}

	/**
	 * Add our components and pages to the "trails" library.
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("trails-hibernate", "org.tynamo.hibernate"));
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration)
	{
		configuration.add("trails-hibernate/" + version, "org/trailsframework/hibernate");
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
												   HibernateDescriptorDecorator hibernateDescriptorDecorator)
	{
		configuration.add("Hibernate", hibernateDescriptorDecorator);
		configuration.add("Annotation", new AnnotationDecorator());
	}

	@SuppressWarnings("unchecked")
	public static void contributeDescriptorService(Configuration<Class> configuration,
												   HibernateSessionSource hibernateSessionSource)
	{

		for (Object classMetadata : hibernateSessionSource.getSessionFactory().getAllClassMetadata().values())
		{
			configuration.add(((ClassMetadata) classMetadata).getMappedClass(EntityMode.POJO));
		}

	}

	public static void contributeTynamoDataTypeAnalyzer(MappedConfiguration<String, String> configuration)
	{
		configuration.add("hidden", "hidden");
		configuration.add("readOnly", "readOnly");
		configuration.add("richText", "fckEditor");
//		configuration.add("name.toLowerCase().endsWith('password')", "passwordEditor"); //USE @DataType("password")
//		configuration.add("string and !large and !identifier", "stringEditor"); //managed by Tapestry
		configuration.add("string and large and !identifier", "longtext");
		configuration.add("date", "dateEditor");
//		configuration.add("numeric and !identifier", "numberEditor"); //managed by Tapestry
		configuration.add("identifier && generated", "readOnly");
		configuration.add("identifier && not(generated) && string", "identifierEditor");
//		configuration.add("identifier && objectReference", "objectReferenceIdentifierEditor");
//		configuration.add("boolean", "booleanEditor"); //managed by Tapestry
//		configuration.add("supportsExtension('org.tynamo.descriptor.extension.EnumReferenceDescriptor')", "enumEditor"); //managed by Tapestry
// @todo: configuration.add("supportsExtension('org.tynamo.descriptor.extension.BlobDescriptorExtension')", "blobEditor");

		configuration.add("objectReference", "single-valued-association" /* (aka: ManyToOne) */);
		configuration.add("collection && not(childRelationship)", "many-valued-association" /* (aka: ManyToMany) */);
		configuration.add("collection && childRelationship", "composition");
		configuration.add("name == 'id'", "readOnly");
		configuration.add("embedded", "embedded");

	}

	public static void contributePropertyDescriptorFactory(Configuration<String> configuration)
	{
		configuration.add("exclude.*");
		configuration.add("class");
	}

	public static void contributeMethodDescriptorFactory(Configuration<String> configuration)
	{
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

	public static HibernateDescriptorDecorator buildHibernateDescriptorDecorator(
												HibernateSessionSource hibernateSessionSource,
												DescriptorFactory descriptorFactory,
												@Symbol(TynamoHibernateSymbols.LARGE_COLUMN_LENGTH)
												final int largeColumnLength,
												@Symbol(TynamoHibernateSymbols.IGNORE_NON_HIBERNATE_TYPES)
												final boolean ignoreNonHibernateTypes)
	{
		return new HibernateDescriptorDecorator(hibernateSessionSource, descriptorFactory, largeColumnLength, ignoreNonHibernateTypes);
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
	{
		configuration.add(TynamoHibernateSymbols.LARGE_COLUMN_LENGTH, "100");
		configuration.add(TynamoHibernateSymbols.IGNORE_NON_HIBERNATE_TYPES, "false");
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
