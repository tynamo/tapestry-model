package org.trailsframework.hibernate.services;

import org.apache.tapestry5.hibernate.HibernateEntityPackageManager;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.hibernate.HibernateTransactionDecorator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;
import org.hibernate.EntityMode;
import org.hibernate.metadata.ClassMetadata;
import org.trailsframework.descriptor.DescriptorDecorator;
import org.trailsframework.descriptor.annotation.AnnotationDecorator;
import org.trailsframework.hibernate.validation.HibernateClassValidatorFactory;
import org.trailsframework.hibernate.validation.HibernateValidationDelegate;

public class TrailsHibernateModule {

	public static void bind(ServiceBinder binder) {
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
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("trails-hibernate", "org.trailsframework.hibernate"));
	}
	
	public static void contributeValidationMessagesSource(OrderedConfiguration<String> configuration)
	{
		configuration.add("Trails", "ValidationMessages");
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
												   ObjectLocator locator)
	{
		configuration.add("Hibernate", locator.autobuild(HibernateDescriptorDecorator.class));
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

	public static void contributeTrailsDataTypeAnalyzer(MappedConfiguration<String, String> configuration)
	{

// @todo: configuration.add("hidden", "hidden");
		configuration.add("readOnly", "readOnly");
		configuration.add("richText", "fckEditor");
//		configuration.add("name.toLowerCase().endsWith('password')", "passwordEditor"); //USE @DataType("password")
//		configuration.add("string and !large and !identifier", "stringEditor"); //managed by Tapestry
		configuration.add("string and large and !identifier", "longtext");
		configuration.add("date", "dateEditor");
//		configuration.add("numeric and !identifier", "numberEditor"); //managed by Tapestry
		configuration.add("identifier", "identifierEditor");
//		configuration.add("boolean", "booleanEditor"); //managed by Tapestry
//		configuration.add("supportsExtension('org.trails.descriptor.extension.EnumReferenceDescriptor')", "enumEditor"); //managed by Tapestry
// @todo: configuration.add("supportsExtension('org.trails.descriptor.extension.BlobDescriptorExtension')", "blobEditor");
		configuration.add("objectReference", "referenceEditor");
		configuration.add("collection && not(childRelationship)", "collectionEditor");
//		configuration.add("collection && childRelationship", "editComposition");
// @todo: configuration.add("embedded", "embedded");

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

	public static void contributeTrailsEntityPackageManager(Configuration<String> configuration, HibernateEntityPackageManager packageManager)
	{
		for (String packageName : packageManager.getPackageNames())
		{
			configuration.add(packageName);
		}
	}


/*
	public static void contributeFieldValidatorSource(MappedConfiguration<String, Validator> configuration) {
			configuration.add("int", new ValidateInt());
	}
*/

	
}
