package org.trailsframework.descriptor;

import java.util.ArrayList;
import java.beans.BeanInfo;


public interface MethodDescriptorFactory
{
	ArrayList<IMethodDescriptor> buildMethodDescriptors(Class type, BeanInfo beanInfo)
			throws Exception;
}
