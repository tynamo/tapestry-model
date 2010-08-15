package org.tynamo.descriptor.factories;

import org.tynamo.descriptor.IMethodDescriptor;

import java.util.ArrayList;
import java.beans.BeanInfo;


public interface MethodDescriptorFactory
{
	ArrayList<IMethodDescriptor> buildMethodDescriptors(Class type, BeanInfo beanInfo)
			throws Exception;
}
