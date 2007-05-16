package org.trails.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import junit.framework.TestCase;

public class FooTest extends TestCase
{
	public void testOrder() throws Exception
	{
		Foo foo = new Foo();
		for (Method fooMethod : Foo.class.getDeclaredMethods())
		{
			//System.out.println(fooMethod.getName());

		}
		BeanInfo beanInfo = Introspector.getBeanInfo(Foo.class);
		for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors())
		{
			//System.out.println(descriptor.getName());
		}
	}
}
