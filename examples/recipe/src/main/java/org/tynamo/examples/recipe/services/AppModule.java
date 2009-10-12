package org.tynamo.examples.recipe.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.hibernate.HibernateModule;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.upload.services.UploadSymbols;
import org.tynamo.builder.Builder;
import org.tynamo.examples.recipe.RecipeBuilder;
import org.tynamo.services.TrailsCoreModule;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to configure and extend
 * Trails, or to place your own service definitions.
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

	}

	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
	{
		// Contributions to ApplicationDefaults will override any contributions to
		// FactoryDefaults (with the same key). Here we're restricting the supported
		// locales to just "en" (English). As you add localised message catalogs and other assets,
		// you can extend this list of locales (it's a comma seperated series of locale names;
		// the first locale name is the default when there's no reasonable match).
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en, es_AR, pt");

		// The factory default is true but during the early stages of an application
		// overriding to false is a good idea. In addition, this is often overridden
		// on the command line as -Dtapestry.production-mode=false
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");

		// Set filesize limit to 2 MB
		configuration.add(UploadSymbols.REQUESTSIZE_MAX, "2048000");
		configuration.add(UploadSymbols.FILESIZE_MAX, "2048000");

	}

	/**
	 * Contributes the package "org.tynamo.examples.recipe.model" to the configuration, so that it will be
	 * scanned for annotated entity classes.
	 */
	public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
	{
		configuration.add("org.tynamo.examples.recipe.model");
	}

	/**
	 * Contributes Builders to the BuilderDirector's builders map.
	 * Check GOF's <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
	 */
	public static void contributeBuilderDirector(MappedConfiguration<Class, Builder> configuration)
	{
		configuration.add(org.tynamo.examples.recipe.model.Recipe.class, new RecipeBuilder());
	}
}