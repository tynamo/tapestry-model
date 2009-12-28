package org.tynamo.services;

public interface EntityCoercerService
{

	String classToString(Class clazz);

	Class stringToClass(String className);

}
