/*
 * Created on Jan 5, 2005
 *
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.tynamo.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import junit.framework.TestCase;

public class TestTest extends TestCase
{

	public void testBlah() throws Exception
	{
		Foo foo = new Foo();
		BeanInfo beanInfo = Introspector.getBeanInfo(Foo.class);
		for (int i = 0; i < beanInfo.getPropertyDescriptors().length; i++)
		{
			PropertyDescriptor descriptor = beanInfo.getPropertyDescriptors()[i];
			//System.out.println(descriptor.getName());
		}
	}
}
