package org.trailsframework.examples.recipe.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.hibernate.HibernateModule;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.hibernate.HibernateTransactionDecorator;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.hibernate.EntityMode;
import org.hibernate.metadata.ClassMetadata;
import org.trailsframework.descriptor.DescriptorDecorator;
import org.trailsframework.descriptor.annotation.AnnotationDecorator;
import org.trailsframework.services.*;
import org.trailsframework.builder.BuilderDirector;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to configure and extend
 * Tapestry, or to place your own service definitions.
 */
@SubModule(value = {TrailsCoreModule.class, HibernateModule.class})
public class AppModule
{

	public static void bind(ServiceBinder binder)
	{
		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(DescriptorService.class, DescriptorServiceImpl.class);
		binder.bind(HibernatePersistenceService.class, HibernatePersistenceServiceImpl.class);
		binder.bind(BuilderDirector.class, BuilderDirector.class);
	}


	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
	{
		// Contributions to ApplicationDefaults will override any contributions to
		// FactoryDefaults (with the same key). Here we're restricting the supported
		// locales to just "en" (English). As you add localised message catalogs and other assets,
		// you can extend this list of locales (it's a comma seperated series of locale names;
		// the first locale name is the default when there's no reasonable match).

		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");

		// The factory default is true but during the early stages of an application
		// overriding to false is a good idea. In addition, this is often overridden
		// on the command line as -Dtapestry.production-mode=false
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
	}


	public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
	{
		configuration.add("org.trailsframework.examples.recipe.model");
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
		configuration.add("Annotation", new AnnotationDecorator());
		configuration.add("Hibernate", locator.autobuild(HibernateDescriptorDecorator.class));
	}

	public static void contributeDescriptorService(Configuration<Class> configuration,
												   HibernateSessionSource hibernateSessionSource)
	{

		for (Object classMetadata : hibernateSessionSource.getSessionFactory().getAllClassMetadata().values())
		{
			configuration.add(((ClassMetadata) classMetadata).getMappedClass(EntityMode.POJO));
		}

	}

	public void contributeTrailsDataTypeAnalyzer(MappedConfiguration<String, String> configuration)
	{


//		configuration.add("hidden", "hidden");
		configuration.add("readOnly", "propertyDisplay");
/*
		configuration.add("richText", "FCKEditor");
		configuration.add("hidden", "hidden");
		configuration.add("name.toLowerCase().endsWith('password')", "passwordEditor");
		configuration.add("string and !large and !identifier", "stringEditor");
		configuration.add("string and large and !identifier", "textAreaEditor");
		configuration.add("date", "dateEditor");
		configuration.add("numeric and !identifier", "numberEditor");
*/
		configuration.add("identifier", "identifierEditor"); //"identifierEditor");
/*
		configuration.add("boolean", "booleanEditor");
		configuration.add("supportsExtension('org.trails.descriptor.extension.EnumReferenceDescriptor')", "enumEditor");
		configuration.add("supportsExtension('org.trails.descriptor.extension.BlobDescriptorExtension')", "blobEditor");
*/
		configuration.add("objectReference", "referenceEditor");
		configuration.add("collection and not(childRelationship)", "collectionEditor");
/*
		configuration.add("embedded", "embedded");
*/
	}

	public void contributePropertyDescriptorFactory(Configuration<String> configuration)
	{
		configuration.add("exclude.*");
		configuration.add("class");
	}

	public void contributeMethodDescriptorFactory(Configuration<String> configuration)
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

}