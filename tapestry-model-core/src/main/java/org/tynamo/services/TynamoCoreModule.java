package org.tynamo.services;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.beaneditor.DataTypeConstants;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.services.PageResponseRenderer;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.services.ChainBuilder;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.plastic.*;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.slf4j.Logger;
import org.tynamo.bindings.ModelBindingFactory;
import org.tynamo.blob.BlobManager;
import org.tynamo.blob.IconResolver;
import org.tynamo.builder.BuilderDirector;
import org.tynamo.common.ModuleProperties;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.annotation.handlers.*;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.descriptor.decorators.TapestryDecorator;
import org.tynamo.descriptor.decorators.TynamoDecorator;
import org.tynamo.descriptor.factories.*;
import org.tynamo.internal.services.BeanModelExtensionBMModifier;
import org.tynamo.internal.services.BeanModelsAnnotationBMModifier;
import org.tynamo.internal.services.BeanModelSourceAdvice;
import org.tynamo.internal.services.BeanModelSourceAdviceImpl;
import org.tynamo.internal.services.BeanModelWorker;
import org.tynamo.internal.services.DefaultExclusionsBMModifier;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.util.Pair;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TynamoCoreModule
{
	private static final String version = ModuleProperties.getVersion(TynamoCoreModule.class);

	public final static String PROPERTY_DISPLAY_BLOCKS = "tynamo/PropertyDisplayBlocks";
	public final static String PROPERTY_EDIT_BLOCKS = "tynamo/PropertyEditBlocks";

	public static void bind(ServiceBinder binder)
	{
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
		binder.bind(SearchFilterBlockSource.class);
		binder.bind(SearchFilterBlockOverrideSource.class);

		binder.bind(BlobManager.class).withId("DefaultBlobManager");

		binder.bind(DescriptorAnnotationHandler.class, BeanModelAnnotationHandler.class).withId("BeanModelAnnotationHandler");
		binder.bind(DescriptorAnnotationHandler.class, BlobDescriptorAnnotationHandler.class).withId("BlobDescriptorAnnotationHandler");
		binder.bind(DescriptorAnnotationHandler.class, ClassDescriptorAnnotationHandler.class).withId("ClassDescriptorAnnotationHandler");
		binder.bind(DescriptorAnnotationHandler.class, CollectionDescriptorAnnotationHandler.class).withId("CollectionDescriptorAnnotationHandler");
		binder.bind(DescriptorAnnotationHandler.class, MethodDescriptorAnnotationHandler.class).withId("MethodDescriptorAnnotationHandler");
		binder.bind(DescriptorAnnotationHandler.class, PropertyDescriptorAnnotationHandler.class).withId("PropertyDescriptorAnnotationHandler");

		binder.bind(BeanModelSourceAdvice.class, BeanModelSourceAdviceImpl.class);
		binder.bind(BeanModelsAnnotationBMModifier.class);
		binder.bind(BeanModelWorker.class);

		binder.bind(IconResolver.class);

	}

	@Match("BeanModelSource")
	public static void adviseBeanModelSource(MethodAdviceReceiver receiver, BeanModelSourceAdvice advice)
	{
		receiver.adviseAllMethods(advice);
	}

	/**
	 * Add our components and pages to the "Tynamo" library.
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("tynamo", "org.tynamo"));
	}

	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration)
	{
		configuration.add("tynamo-" + version, "org/tynamo");
	}


	/**
	 * Contribution to the BeanBlockSource service to tell the BeanEditForm component about the editors.
	 */
	public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration)
	{
		configuration.add(new EditBlockContribution("nonVisual", PROPERTY_EDIT_BLOCKS, "nonVisual"));
		configuration.add(new EditBlockContribution("formatted-date", "PropertyEditBlocks", DataTypeConstants.DATE));
		configuration.add(new EditBlockContribution("formatted-number", "PropertyEditBlocks", DataTypeConstants.NUMBER));
		configuration.add(new EditBlockContribution("ckeditor", PROPERTY_EDIT_BLOCKS, "ckeditor"));

		configuration.add(new EditBlockContribution("readOnly", PROPERTY_EDIT_BLOCKS, "readOnly"));
		configuration.add(new EditBlockContribution("single-valued-association", PROPERTY_EDIT_BLOCKS, "select"));
		configuration.add(new EditBlockContribution("identifierEditor", PROPERTY_EDIT_BLOCKS, "identifierEditor"));
		configuration.add(new EditBlockContribution("many-valued-association", PROPERTY_EDIT_BLOCKS, "palette"));
		configuration.add(new EditBlockContribution("composition", PROPERTY_EDIT_BLOCKS, "editComposition"));
		configuration.add(new EditBlockContribution("embedded", PROPERTY_EDIT_BLOCKS, "embedded"));
		configuration.add(new EditBlockContribution("blob", PROPERTY_EDIT_BLOCKS, "blob"));

		configuration.add(new DisplayBlockContribution("nonVisual", PROPERTY_DISPLAY_BLOCKS, "nonVisual"));
		configuration.add(new DisplayBlockContribution("formatted-date", PROPERTY_DISPLAY_BLOCKS, "date"));
		configuration.add(new DisplayBlockContribution("formatted-number", PROPERTY_DISPLAY_BLOCKS, "number"));

		configuration.add(new DisplayBlockContribution("embedded", PROPERTY_DISPLAY_BLOCKS, "embedded"));
		configuration.add(new DisplayBlockContribution("composition", PROPERTY_DISPLAY_BLOCKS, "composition"));
		configuration.add(new DisplayBlockContribution("blob", PROPERTY_DISPLAY_BLOCKS, "download"));
		configuration.add(new DisplayBlockContribution("ckeditor", PROPERTY_DISPLAY_BLOCKS, "ckeditor"));
	}

	public static void contributeBeanBlockOverrideSource(Configuration<BeanBlockContribution> configuration)
	{
		configuration.add(new EditBlockContribution("enum", PROPERTY_EDIT_BLOCKS, "select"));
	}

	@Contribute(SearchFilterBlockSource.class)
	public static void provideDefaultSearchFilterBlocks(Configuration<SearchFilterBlockContribution> configuration) {
		configuration.add(new SearchFilterBlockContribution(DataTypeConstants.TEXT, "tynamo/PropertySearchFilterBlocks",
			DataTypeConstants.TEXT));
		configuration.add(new SearchFilterBlockContribution("formatted-number", "tynamo/PropertySearchFilterBlocks",
			DataTypeConstants.NUMBER));
		configuration.add(new SearchFilterBlockContribution(DataTypeConstants.NUMBER, "tynamo/PropertySearchFilterBlocks",
			DataTypeConstants.NUMBER));
		configuration.add(new SearchFilterBlockContribution(DataTypeConstants.ENUM, "tynamo/PropertySearchFilterBlocks",
			DataTypeConstants.ENUM));
		configuration.add(new SearchFilterBlockContribution(DataTypeConstants.BOOLEAN, "tynamo/PropertySearchFilterBlocks",
			DataTypeConstants.BOOLEAN));
		configuration.add(new SearchFilterBlockContribution(DataTypeConstants.DATE, "tynamo/PropertySearchFilterBlocks",
			DataTypeConstants.DATE));
	}

	public static void contributeDataTypeAnalyzer(OrderedConfiguration<DataTypeAnalyzer> configuration,
	                                              @InjectService("TynamoDataTypeAnalyzer")
	                                              DataTypeAnalyzer tynamoDataTypeAnalyzer)
	{
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
	                                         @InjectService("EntityCoercerService")
	                                         EntityCoercerService entityCoercerService)
	{
		configuration.add(CoercionTuple.create(Class.class, String.class, new ClassToStringCoercion(entityCoercerService)));
		configuration.add(CoercionTuple.create(String.class, Class.class, new StringToClassCoercion(entityCoercerService)));
	}

	public static void contributeDescriptorFactory(OrderedConfiguration<DescriptorDecorator> configuration,
	                                               PropertyAccess propertyAccess, ObjectLocator locator)
	{
		configuration.add("TynamoDecorator", new TynamoDecorator(locator));
		configuration.add("TapestryDecorator", new TapestryDecorator(propertyAccess));
	}

	public static void contributeTynamoDataTypeAnalyzer(OrderedConfiguration<Pair> configuration)
	{
		configuration.add("nonVisual", newPair(TynamoDataTypeAnalyzerPredicates.nonVisual, "nonVisual"));
		configuration.add("readOnly", newPair(TynamoDataTypeAnalyzerPredicates.readOnly, "readOnly"));
		configuration.add("generatedId", newPair(TynamoDataTypeAnalyzerPredicates.generatedId, "readOnly"));
		configuration.add("assignedId", newPair(TynamoDataTypeAnalyzerPredicates.assignedId, "identifierEditor"));
		configuration.add("richText", newPair(TynamoDataTypeAnalyzerPredicates.richText, "ckeditor"));
		configuration.add("password", newPair(TynamoDataTypeAnalyzerPredicates.password, "password"));
		configuration.add("date", newPair(TynamoDataTypeAnalyzerPredicates.date, "formatted-date"));
		configuration.add("number", newPair(TynamoDataTypeAnalyzerPredicates.number, "formatted-number"));
		configuration.add("longtext", newPair(TynamoDataTypeAnalyzerPredicates.longtext, "longtext"));
//		configuration.add("", newPair("identifier && objectReference", "objectReferenceIdentifierEditor");
		configuration.add("enum", newPair(TynamoDataTypeAnalyzerPredicates.enumi, "enum")); // overrides Tapestry's enum
		configuration.add("blob", newPair(TynamoDataTypeAnalyzerPredicates.blob, "blob"));
		configuration.add("manyToOne", newPair(TynamoDataTypeAnalyzerPredicates.manyToOne, "single-valued-association"));
		configuration.add("manyToMany", newPair(TynamoDataTypeAnalyzerPredicates.manyToMany, "many-valued-association"));
		configuration.add("composition", newPair(TynamoDataTypeAnalyzerPredicates.composition, "composition"));
		configuration.add("embedded", newPair(TynamoDataTypeAnalyzerPredicates.embedded, "embedded"));
	}

	private static Pair<Predicate<TynamoPropertyDescriptor>, String> newPair(Predicate<TynamoPropertyDescriptor> predicate, String value)
	{
		return new Pair<Predicate<TynamoPropertyDescriptor>, String>(predicate, value);
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

	@Contribute(EntityCoercerService.class)
	public static void contributeEntityCoercerService(MappedConfiguration<String, Class> configuration,
	                                                  DescriptorService descriptorService)
	{
		for (TynamoClassDescriptor classDescriptor : descriptorService.getAllDescriptors())
		{
			Class clazz = classDescriptor.getBeanType();
			String simpleName = clazz.getSimpleName().toLowerCase();
			configuration.add(simpleName, clazz);
		}
	}

	@Contribute(BindingSource.class)
	public void contributeBindingSource(MappedConfiguration<String, BindingFactory> configuration,
	                                    @Autobuild ModelBindingFactory modelBindingFactory)
	{
		configuration.add("mb", modelBindingFactory);
	}

	public SearchableGridDataSourceProvider buildSearchableGridDataSourceProvider(
			final Logger logger,
			final AlertManager alertManager,
			final PersistenceService persistenceService) {
		// naive implementation to be overridden in persistence-specific sub modules
		return new SearchableGridDataSourceProvider() {
			@Override
			public GridDataSource createGridDataSource(Class entityType) {
				String message = "There is no search support configured, you are using a SearchableGridDataSourceProvider" +
						" naive implementation meant to be overridden in persistence-specific sub modules";

				alertManager.error(message);
				logger.error(message);

				return persistenceService.getGridDataSource(entityType);
			}

			@Override
			public GridDataSource createGridDataSource(Class entityType,
				Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap,
				List<TynamoPropertyDescriptor> searchablePropertyDescriptors, String searchTerm) {
				return createGridDataSource(entityType);
			}

			@Override
			public GridDataSource createGridDataSource(Class entityType, Set includedIds,
				Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap) {
				return createGridDataSource(entityType);
			}

		};
	}

	@Primary
	@Contribute(ComponentClassTransformWorker2.class)
	public static void provideTransformWorkers(OrderedConfiguration<ComponentClassTransformWorker2> configuration, BeanModelWorker beanModelWorker)
	{
		configuration.add("BeanModelWorker", beanModelWorker);
	}

	/**
	 * Builds the {@link BeanModelModifier} service as a chain of command.
	 */
	@Marker(Primary.class)
	public BeanModelModifier buildBeanModelModifier(List<BeanModelModifier> configuration, ChainBuilder chainBuilder)
	{
		return chainBuilder.build(BeanModelModifier.class, configuration);
	}

	@Primary
	@Contribute(BeanModelModifier.class)
	public static void addBeanModelModifiers(OrderedConfiguration<BeanModelModifier> configuration,
	                                         BeanModelsAnnotationBMModifier pageAnnotationsModifier,
	                                         @Autobuild DefaultExclusionsBMModifier defaultModifier,
	                                         @Autobuild BeanModelExtensionBMModifier extensionModifier)
	{
		configuration.add("pages", pageAnnotationsModifier);
		configuration.add("entities", extensionModifier);
		configuration.add("defaults", defaultModifier, "after:*");
	}

	@Contribute(IconResolver.class)
	public static void addIcons(MappedConfiguration<String, String> configuration){

		configuration.add("application/octet-stream", "org/tynamo/components/blob/image/asset/icgen.gif");

		configuration.add("application/x-zip-compressed", "org/tynamo/components/blob/image/asset/winzip.gif");
		configuration.add("application/pdf", "org/tynamo/components/blob/image/asset/icadobe.gif");
		configuration.add("application/msword", "org/tynamo/components/blob/image/asset/icdoc.gif");
		configuration.add("application/vnd.visio", "org/tynamo/components/blob/image/asset/icdoc.gif");
		configuration.add("application/vnd.ms-powerpoint", "org/tynamo/components/blob/image/asset/icppt.gif");
		configuration.add("application/vnd.ms-excel", "org/tynamo/components/blob/image/asset/icxls.gif");
		configuration.add("text/html", "org/tynamo/components/blob/image/asset/ichtm.gif");
		configuration.add("text/plain", "org/tynamo/components/blob/image/asset/ictxt.gif");
		configuration.add("text/css", "org/tynamo/components/blob/image/asset/ictxt.gif");
		configuration.add("text/xml", "org/tynamo/components/blob/image/asset/icxml.gif");
		configuration.add("image/tiff", "org/tynamo/components/blob/image/asset/icgen.gif");
		configuration.add("video/avi", "org/tynamo/components/blob/image/asset/icwmp.gif");
		configuration.add("video/mpeg", "org/tynamo/components/blob/image/asset/icwmp.gif");
		configuration.add("video/mp4", "org/tynamo/components/blob/image/asset/icwmp.gif");
		configuration.add("video/quicktime", "org/tynamo/components/blob/image/asset/icwmp.gif");
		configuration.add("video/x-ms-wmv", "org/tynamo/components/blob/image/asset/icwmp.gif");
	}


	/**
	 * TYNAMO-224
	 *
	 * Discards all page persistent field changes if there is any exception in renderPageResponse
	 */
	@Match("PageResponseRenderer")
	public static void advisePageResponseRenderer(MethodAdviceReceiver receiver, final Logger logger) throws NoSuchMethodException
	{
		Method method = PageResponseRenderer.class.getMethod("renderPageResponse", Page.class);
		receiver.adviseMethod(method, new org.apache.tapestry5.plastic.MethodAdvice()
		{
			@Override
			public void advise(MethodInvocation methodInvocation)
			{
				try
				{
					methodInvocation.proceed();
				} catch (RuntimeException e)
				{
					Page page = (Page) methodInvocation.getParameter(0);
					page.discardPersistentFieldChanges();
					logger.info(String.format("discarding all %s page persistent field changes due to a %s", page.getName(), e.getClass().getSimpleName()));
					throw e;
				}
			}
		});
	}


}
