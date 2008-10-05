package org.trailsframework.descriptor;

import java.util.ArrayList;
import java.beans.BeanInfo;

public interface PropertyDescriptorFactory
{
	ArrayList<IPropertyDescriptor> buildPropertyDescriptors(Class beanType, BeanInfo beanInfo) throws Exception;
}
