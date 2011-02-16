package org.tynamo.services;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.tapestry5.ioc.services.Coercion;

import java.util.Map;

public class EntityCoercerServiceImpl implements EntityCoercerService
{

	private BidiMap aliases;

	public EntityCoercerServiceImpl(Map<String, Class> aliases)
	{
		this.aliases = new DualHashBidiMap(aliases);
	}

	public String classToString(Class clazz)
	{
		return (String) aliases.inverseBidiMap().get(clazz);
	}

	public Class stringToClass(String simpleName)
	{
		return (Class) aliases.get(simpleName);
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
