package org.tynamo.descriptor.factories;

import org.tynamo.descriptor.IMethodDescriptor;

import java.beans.BeanInfo;
import java.util.ArrayList;


public interface MethodDescriptorFactory
{
	ArrayList<IMethodDescriptor> buildMethodDescriptors(Class type, BeanInfo beanInfo);
}
