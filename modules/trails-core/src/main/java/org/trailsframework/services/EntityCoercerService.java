package org.trailsframework.services;

public interface EntityCoercerService
{

	String classToString(Class clazz);

	Class stringToClass(String className);

}
