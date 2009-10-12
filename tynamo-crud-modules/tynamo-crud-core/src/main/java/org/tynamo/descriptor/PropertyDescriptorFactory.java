package org.tynamo.descriptor;

import java.util.ArrayList;
import java.beans.BeanInfo;

public interface PropertyDescriptorFactory
{
	ArrayList<TrailsPropertyDescriptor> buildPropertyDescriptors(Class beanType, BeanInfo beanInfo) throws Exception;
}
