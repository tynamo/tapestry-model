package org.trailsframework.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.apache.tapestry5.services.LibraryMapping;

public class TrailsCoreModule
{

	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("trails", "org.trailsframework"));
	}

	/**
	 * Contribution to the BeanBlockSource service to tell the BeanEditForm component about the editors.
	 */
	public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration)
	{
		configuration.add(new BeanBlockContribution("referenceEditor", "trails/Editors", "select", true));
	}


	/**
	 * <dl> <dt>Annotation</dt> <dd>Checks for {@link org.apache.tapestry5.beaneditor.DataType} annotation</dd>
	 * <dt>Default  (ordered last)</dt> <dd>{@link org.apache.tapestry5.internal.services.DefaultDataTypeAnalyzer}
	 * service ({@link #contributeDefaultDataTypeAnalyzer(org.apache.tapestry5.ioc.MappedConfiguration)} })</dd> </dl>
	 */
	public static void contributeDataTypeAnalyzer(OrderedConfiguration<DataTypeAnalyzer> configuration,
												  @InjectService("DefaultDataTypeAnalyzer")
												  DataTypeAnalyzer defaultDataTypeAnalyzer,
												  ObjectLocator locator)
	{
		configuration.add("Trails", locator.autobuild(TrailsDataTypeAnalizer.class));
		configuration.add("Default", defaultDataTypeAnalyzer, "after:*");
	}

	/**
	 * Contributes a set of standard type coercions to the {@link org.apache.tapestry5.ioc.services.TypeCoercer} service:
	 * <ul>
	 *     <li>Class to String</li>
	 *     <li>String to Double</li>
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


}
