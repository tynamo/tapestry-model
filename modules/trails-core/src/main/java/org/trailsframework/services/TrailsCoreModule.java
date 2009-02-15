package org.trailsframework.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.apache.tapestry5.services.LibraryMapping;
import org.trailsframework.VersionedModule;
import org.trailsframework.builder.BuilderDirector;
import org.trailsframework.descriptor.DescriptorFactory;
import org.trailsframework.descriptor.MethodDescriptorFactory;
import org.trailsframework.descriptor.MethodDescriptorFactoryImpl;
import org.trailsframework.descriptor.PropertyDescriptorFactory;
import org.trailsframework.descriptor.PropertyDescriptorFactoryImpl;
import org.trailsframework.descriptor.ReflectionDescriptorFactory;

public class TrailsCoreModule extends VersionedModule {

	public static void bind(ServiceBinder binder) {
		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(BuilderDirector.class, BuilderDirector.class);
		binder.bind(DescriptorFactory.class, ReflectionDescriptorFactory.class);
		binder.bind(PropertyDescriptorFactory.class, PropertyDescriptorFactoryImpl.class);
		binder.bind(MethodDescriptorFactory.class, MethodDescriptorFactoryImpl.class);
		binder.bind(EntityCoercerService.class, EntityCoercerServiceImpl.class);
		binder.bind(DescriptorService.class, DescriptorServiceImpl.class);

	}

	/**
	 * Add our components and pages to the "trails" library.
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("trails", "org.trailsframework"));
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
		configuration.add("trails/" + version, "org/trailsframework");
	}
	
	
	/**
	 * Contribution to the BeanBlockSource service to tell the BeanEditForm component about the editors.
	 */
	public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
		configuration.add(new BeanBlockContribution("dateEditor", "trails/Editors", "date", true));
		configuration.add(new BeanBlockContribution("fckEditor", "trails/Editors", "fckEditor", true));
		configuration.add(new BeanBlockContribution("readOnly", "trails/Editors", "readOnly", true));
		configuration.add(new BeanBlockContribution("referenceEditor", "trails/Editors", "select", true));
		configuration.add(new BeanBlockContribution("identifierEditor", "trails/Editors", "identifierEditor", true));
		configuration.add(new BeanBlockContribution("collectionEditor", "trails/Editors", "palette", true));
		configuration.add(new BeanBlockContribution("editComposition", "trails/Editors", "editComposition", true));
	}

	/**
	 * <dl>
	 * <dt>Annotation</dt>
	 * <dd>Checks for {@link org.apache.tapestry5.beaneditor.DataType} annotation</dd>
	 * <dt>Default (ordered last)</dt>
	 * <dd>{@link org.apache.tapestry5.internal.services.DefaultDataTypeAnalyzer} service (
	 * {@link #contributeDefaultDataTypeAnalyzer(org.apache.tapestry5.ioc.MappedConfiguration)} )</dd>
	 * </dl>
	 */
	public static void contributeDataTypeAnalyzer(OrderedConfiguration<DataTypeAnalyzer> configuration,
			@InjectService("DefaultDataTypeAnalyzer") DataTypeAnalyzer defaultDataTypeAnalyzer,
			@InjectService("TrailsDataTypeAnalyzer") DataTypeAnalyzer trailsDataTypeAnalyzer) {
		configuration.add("Trails", trailsDataTypeAnalyzer, "before:Default");
	}

	/**
	 * Contributes a set of standard type coercions to the {@link org.apache.tapestry5.ioc.services.TypeCoercer} service:
	 * <ul>
	 * <li>Class to String</li>
	 * <li>String to Double</li>
	 * </ul>
	 */
	@SuppressWarnings("unchecked")
	public static void contributeTypeCoercer(final Configuration<CoercionTuple> configuration,
			@InjectService("EntityCoercerService") EntityCoercerService entityCoercerService) {
		configuration.add(new CoercionTuple<Class, String>(Class.class, String.class, new ClassToStringCoercion(entityCoercerService)));
		configuration.add(new CoercionTuple<String, Class>(String.class, Class.class, new StringToClassCoercion(entityCoercerService)));
	}

	public static TrailsDataTypeAnalyzer buildTrailsDataTypeAnalyzer(ServiceResources resources) {
		return resources.autobuild(TrailsDataTypeAnalyzer.class);
	}

}
