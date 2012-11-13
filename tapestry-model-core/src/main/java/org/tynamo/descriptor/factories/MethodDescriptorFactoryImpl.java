package org.tynamo.descriptor.factories;

import org.tynamo.descriptor.IMethodDescriptor;
import org.tynamo.descriptor.TynamoMethodDescriptorImpl;
import org.tynamo.util.Utils;

import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.util.ArrayList;
import java.util.Collection;


public class MethodDescriptorFactoryImpl implements MethodDescriptorFactory
{

	private final Collection<String> methodExcludes;

	public MethodDescriptorFactoryImpl(Collection<String> methodExcludes)
	{
		this.methodExcludes = methodExcludes;
	}


	public ArrayList<IMethodDescriptor> buildMethodDescriptors(Class type, BeanInfo beanInfo)
	{
		ArrayList<IMethodDescriptor> result = new ArrayList<IMethodDescriptor>();
		for (MethodDescriptor beanMethodDescriptor : beanInfo.getMethodDescriptors())
		{
			if (!Utils.isExcluded(beanMethodDescriptor.getMethod().getName(), methodExcludes))
			{
				TynamoMethodDescriptorImpl methodDescriptor = new TynamoMethodDescriptorImpl(type,
						beanMethodDescriptor.getMethod().getName(), beanMethodDescriptor.getMethod().getReturnType(),
						beanMethodDescriptor.getMethod().getParameterTypes());
//				methodDescriptor.setDisplayName(Utils.unCamelCase(beanMethodDescriptor.getDisplayName()));
				result.add(methodDescriptor);
			}
		}
		return result;
	}
}