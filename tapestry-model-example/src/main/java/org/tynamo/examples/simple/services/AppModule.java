package org.tynamo.examples.simple.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.beanvalidator.modules.BeanValidatorModule;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.modules.TapestryIOCModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.modules.TapestryModule;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.BeanBlockSource;
import org.apache.tapestry5.services.DisplayBlockContribution;
import org.apache.tapestry5.upload.modules.UploadModule;
import org.apache.tapestry5.upload.services.UploadSymbols;
import org.tynamo.builder.Builder;
import org.tynamo.builder.BuilderDirector;
import org.tynamo.ckeditor.CKEditorModule;
import org.tynamo.routing.services.RoutingModule;
import org.tynamo.services.TynamoCoreModule;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@SubModule(value = {
		TapestryModule.class,
		TapestryIOCModule.class,
		BeanValidatorModule.class,
		TynamoCoreModule.class,
		RoutingModule.class,
		CKEditorModule.class,
		UploadModule.class
})
public class AppModule
{
	public static void bind(ServiceBinder binder)
	{
		// binder.bind(MyServiceInterface.class, MyServiceImpl.class);

		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.
	}

	@Contribute(SymbolProvider.class)
	@ApplicationDefaults
	public static void provideSymbols(MappedConfiguration<String, String> configuration)
	{
		// Contributions to ApplicationDefaults will override any contributions to
		// FactoryDefaults (with the same key). Here we're restricting the supported
		// locales to just "en" (English). As you add localised message catalogs and other assets,
		// you can extend this list of locales (it's a comma separated series of locale names;
		// the first locale name is the default when there's no reasonable match).
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en, es");

		// Set filesize limit to 2 MB
		configuration.add(UploadSymbols.REQUESTSIZE_MAX, "2048000");
		configuration.add(UploadSymbols.FILESIZE_MAX, "2048000");
	}

	@Contribute(SymbolProvider.class)
	@FactoryDefaults
	public static void overrideFactorySymbols(MappedConfiguration<String, Object> configuration)
	{
		// The application version is incorprated into URLs for most assets. Web
		// browsers will cache assets because of the far future expires header.
		// If existing assets change (or if the Tapestry version changes) you
		// should also change this number, to force the browser to download new
		// versions. This overrides Tapesty's default (a random hexadecimal
		// number), but may be further overriden by DevelopmentModule or QaModule
		// by adding the same key in the contributeApplicationDefaults method.
		configuration.override(SymbolConstants.APPLICATION_VERSION, "0.0.1-SNAPSHOT");
	}

	/**
	 * Contribution to the BeanBlockSource service to tell the BeanEditForm component about the editors.
	 */
	@Contribute(BeanBlockSource.class)
	public static void addCustomBlocks(Configuration<BeanBlockContribution> configuration)
	{
		configuration.add(new DisplayBlockContribution("boolean", "blocks/DisplayBlocks", "check"));
		configuration.add(new DisplayBlockContribution("single-valued-association", "blocks/DisplayBlocks", "showPageLink"));
		configuration.add(new DisplayBlockContribution("many-valued-association", "blocks/DisplayBlocks", "showPageLinks"));
	}

	/**
	 * Contributes Builders to the BuilderDirector's builders map.
	 * Check GOF's <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
	 */
	@Contribute(BuilderDirector.class)
	public static void addBuilders(MappedConfiguration<Class, Builder> configuration)
	{
//		configuration.add(org.tynamo.examples.recipe.model.Recipe.class, new RecipeBuilder());
	}

}
