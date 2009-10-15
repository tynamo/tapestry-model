package org.tynamo.descriptor;

import java.util.ArrayList;
import java.beans.BeanInfo;

public interface PropertyDescriptorFactory
{
	ArrayList<TynamoPropertyDescriptor> buildPropertyDescriptors(Class beanType, BeanInfo beanInfo) throws Exception;
}
