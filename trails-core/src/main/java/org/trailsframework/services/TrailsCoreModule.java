package org.trailsframework.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.apache.tapestry5.services.LibraryMapping;
import org.trailsframework.descriptor.*;

public class TrailsCoreModule
{

	public static void bind(ServiceBinder binder)
	{
		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(DescriptorFactory.class, ReflectionDescriptorFactory.class);
		binder.bind(PropertyDescriptorFactory.class, PropertyDescriptorFactoryImpl.class);
		binder.bind(MethodDescriptorFactory.class, MethodDescriptorFactoryImpl.class);

	}

	/**
	 * Add our components and pages to the "trails" library.
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("trails", "org.trailsframework"));
	}

	/**
	 * Contribution to the BeanBlockSource service to tell the BeanEditForm component about the editors.
	 */
	public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration)
	{
		configuration.add(new BeanBlockContribution("dateEditor", "trails/Editors", "date", true));
		configuration.add(new BeanBlockContribution("fckEditor", "trails/Editors", "fckEditor", true));
		configuration.add(new BeanBlockContribution("readOnly", "trails/Editors", "readOnly", true));
		configuration.add(new BeanBlockContribution("referenceEditor", "trails/Editors", "select", true));
		configuration.add(new BeanBlockContribution("identifierEditor", "trails/Editors", "identifierEditor", true));
		configuration.add(new BeanBlockContribution("collectionEditor", "trails/Editors", "palette", true));
		configuration.add(new BeanBlockContribution("editComposition", "trails/Editors", "editComposition", true));
	}


	/**
	 * <dl> <dt>Annotation</dt> <dd>Checks for {@link org.apache.tapestry5.beaneditor.DataType} annotation</dd>
	 * <dt>Default  (ordered last)</dt> <dd>{@link org.apache.tapestry5.internal.services.DefaultDataTypeAnalyzer}
	 * service ({@link #contributeDefaultDataTypeAnalyzer(org.apache.tapestry5.ioc.MappedConfiguration)} })</dd> </dl>
	 */
	public static void contributeDataTypeAnalyzer(OrderedConfiguration<DataTypeAnalyzer> configuration,
												  @InjectService("DefaultDataTypeAnalyzer")
												  DataTypeAnalyzer defaultDataTypeAnalyzer,
												  @InjectService("TrailsDataTypeAnalyzer")
												  DataTypeAnalyzer trailsDataTypeAnalyzer)
	{
		configuration.add("Trails", trailsDataTypeAnalyzer);
		configuration.add("Default", defaultDataTypeAnalyzer, "after:*");
	}

	/**
	 * Contributes a set of standard type coercions to the {@link org.apache.tapestry5.ioc.services.TypeCoercer} service:
	 * <ul>
	 * <li>Class to String</li>
	 * <li>String to Double</li>
	 * </ul>
	 */
	public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration)
	{
		Coercion<Class, String> classToString = new Coercion<Class, String>()
		{
			public String coerce(Class clazz)
			{
				return clazz.getName();
			}
		};

		Coercion<String, Class> stringToClass = new Coercion<String, Class>()
		{
			public Class coerce(String className)
			{
				try
				{
					return Class.forName(className);

				} catch (ClassNotFoundException e)
				{
					throw new RuntimeException("Coercion failed!", e);
				}
			}
		};

		configuration.add(new CoercionTuple<Class, String>(Class.class, String.class, classToString));
		configuration.add(new CoercionTuple<String, Class>(String.class, Class.class, stringToClass));
	}

	public TrailsDataTypeAnalyzer buildTrailsDataTypeAnalyzer(ServiceResources resources)
	{
		return resources.autobuild(TrailsDataTypeAnalyzer.class);
	}

}
