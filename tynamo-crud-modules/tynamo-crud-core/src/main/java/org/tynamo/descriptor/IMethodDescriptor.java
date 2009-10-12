package org.tynamo.descriptor;

import java.lang.reflect.Method;


public interface IMethodDescriptor extends Descriptor
{

	Class[] getArgumentTypes();

	String getName();

	Class getBeanType();

	Method getMethod() throws NoSuchMethodException;

}