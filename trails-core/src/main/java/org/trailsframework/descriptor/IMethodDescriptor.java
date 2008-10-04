package org.trailsframework.descriptor;

import java.lang.reflect.Method;


public interface IMethodDescriptor extends IDescriptor
{

	Class[] getArgumentTypes();

	String getName();

	Class getBeanType();

	Method getMethod() throws NoSuchMethodException;

}