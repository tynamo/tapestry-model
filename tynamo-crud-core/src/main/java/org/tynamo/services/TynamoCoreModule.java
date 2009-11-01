package org.tynamo.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.apache.tapestry5.services.LibraryMapping;
import org.tynamo.VersionedModule;
import org.tynamo.blob.DefaultBlobManager;
import org.tynamo.blob.BlobManager;
import org.tynamo.builder.BuilderDirector;
import org.tynamo.descriptor.DescriptorFactory;
import org.tynamo.descriptor.MethodDescriptorFactory;
import org.tynamo.descriptor.MethodDescriptorFactoryImpl;
import org.tynamo.descriptor.PropertyDescriptorFactory;
import org.tynamo.descriptor.PropertyDescriptorFactoryImpl;
import org.tynamo.descriptor.ReflectionDescriptorFactory;

public class TynamoCoreModule extends VersionedModule {

	public final static String PROPERTY_DISPLAY_BLOCKS = "tynamo/PropertyDisplayBlocks";
	public final static String PROPERTY_EDIT_BLOCKS = "tynamo/Editors";

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
		binder.bind(TynamoDataTypeAnalyzer.class, TynamoDataTypeAnalyzer.class);

		binder.bind(BlobManager.class, DefaultBlobManager.class);

	}

	/**
	 * Add our components and pages to the "Tynamo" library.
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("tynamo", "org.tynamo"));
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
		configuration.add("tynamo/" + version, "org/tynamo");
	}


	/**
	 * Contribution to the BeanBlockSource service to tell the BeanEditForm component about the editors.
	 */
	public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
		configuration.add(new BeanBlockContribution("hidden", PROPERTY_EDIT_BLOCKS, "hidden", true));
		configuration.add(new BeanBlockContribution("dateEditor", PROPERTY_EDIT_BLOCKS, "date", true));
		configuration.add(new BeanBlockContribution("fckEditor", PROPERTY_EDIT_BLOCKS, "fckEditor", true));
		configuration.add(new BeanBlockContribution("readOnly", PROPERTY_EDIT_BLOCKS, "readOnly", true));
		configuration.add(new BeanBlockContribution("single-valued-association", PROPERTY_EDIT_BLOCKS, "select", true));
		configuration.add(new BeanBlockContribution("identifierEditor", PROPERTY_EDIT_BLOCKS, "identifierEditor", true));
		configuration.add(new BeanBlockContribution("many-valued-association", PROPERTY_EDIT_BLOCKS, "palette", true));
		configuration.add(new BeanBlockContribution("composition", PROPERTY_EDIT_BLOCKS, "editComposition", true));
		configuration.add(new BeanBlockContribution("embedded", PROPERTY_EDIT_BLOCKS, "embedded", true));

		configuration.add(new BeanBlockContribution("hidden", PROPERTY_DISPLAY_BLOCKS, "hidden", false));
		configuration.add(new BeanBlockContribution("single-valued-association", PROPERTY_DISPLAY_BLOCKS, "showPageLink", false));
		configuration.add(new BeanBlockContribution("many-valued-association", PROPERTY_DISPLAY_BLOCKS, "showPageLinks", false));
		configuration.add(new BeanBlockContribution("composition", PROPERTY_DISPLAY_BLOCKS, "showPageLinks", false));

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
			@InjectService("TynamoDataTypeAnalyzer") DataTypeAnalyzer tynamoDataTypeAnalyzer) {
		configuration.add("Tynamo", tynamoDataTypeAnalyzer, "before:Default");
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

}
