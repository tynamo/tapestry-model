package org.tynamo.descriptor.factories;

import org.tynamo.descriptor.TynamoPropertyDescriptor;

import java.util.ArrayList;
import java.beans.BeanInfo;

public interface PropertyDescriptorFactory
{
	ArrayList<TynamoPropertyDescriptor> buildPropertyDescriptors(Class beanType, BeanInfo beanInfo) throws Exception;
}
