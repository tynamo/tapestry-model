package org.tynamo.descriptor.factories;

import org.tynamo.descriptor.TynamoPropertyDescriptor;

import java.beans.BeanInfo;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public interface PropertyDescriptorFactory
{
	ArrayList<TynamoPropertyDescriptor> buildPropertyDescriptors(Class beanType, BeanInfo beanInfo) throws InvocationTargetException, IllegalAccessException;
}
