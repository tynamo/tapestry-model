package org.tynamo.services;

import org.apache.tapestry5.ioc.services.Coercion;
import org.tynamo.descriptor.TynamoClassDescriptor;

import java.util.HashMap;
import java.util.Map;

public class EntityCoercerServiceImpl implements EntityCoercerService
{

	Map<String, Class> stringToClass;
	Map<Class, String> classToString;

	/**
	 *
	 * @param descriptorService used to initialize the mappings to avoid TRAILS-173
	 */
	public EntityCoercerServiceImpl(DescriptorService descriptorService)
	{
		stringToClass = new HashMap<String, Class>();
		classToString = new HashMap<Class, String>();

		for (TynamoClassDescriptor classDescriptor : descriptorService.getAllDescriptors())
		{
			classToString(classDescriptor.getBeanType());
		}
	}

	public String classToString(Class clazz)
	{
		String simpleName = classToString.get(clazz);
		if (simpleName == null)
		{
			simpleName = clazz.getSimpleName().toLowerCase();
			classToString.put(clazz, simpleName);
			stringToClass.put(simpleName, clazz);
		}
		return simpleName;
	}

	public Class stringToClass(String simpleName)
	{
		return stringToClass.get(simpleName);
	}
}

class ClassToStringCoercion implements Coercion<Class, String>
{

	EntityCoercerService entityCoercerService;

	public ClassToStringCoercion(final EntityCoercerService entityCoercerService)
	{
		this.entityCoercerService = entityCoercerService;
	}

	public String coerce(Class clazz)
	{
		return entityCoercerService.classToString(clazz);
	}

}

class StringToClassCoercion implements Coercion<String, Class>
{

	EntityCoercerService entityCoercerService;

	public StringToClassCoercion(final EntityCoercerService entityCoercerService)
	{
		this.entityCoercerService = entityCoercerService;
	}

	public Class coerce(String simpleName)
	{
		return entityCoercerService.stringToClass(simpleName);
	}

}
