/*
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
package org.tynamo.descriptor;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.descriptor.extension.DescriptorExtension;
import org.tynamo.test.Foo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;


public class PropertyDescriptorTest extends Assert
{
	@Test
	public void testIsNumeric() throws Exception
	{

		TynamoPropertyDescriptor propertyDescriptor = new TynamoPropertyDescriptorImpl(Foo.class, Double.class);
		assertTrue(propertyDescriptor.isNumeric());
	}

	@Test public void testIsBoolean() throws Exception
	{

		TynamoPropertyDescriptor propertyDescriptor = new TynamoPropertyDescriptorImpl(Foo.class, boolean.class);
		assertTrue(propertyDescriptor.isBoolean());
	}

/*
	@Test public void testDisplayName() throws Exception
	{
		ReflectionDescriptorFactory descriptorFactory = new ReflectionDescriptorFactory();
		TynamoClassDescriptor classDescriptor = descriptorFactory.buildClassDescriptor(Foo.class);
		TynamoPropertyDescriptor propertyDescriptor = classDescriptor.getPropertyDescriptor("multiWordProperty");
		// By default, unCamelCase class and property names
		assertEquals("display name", "Multi Word Property", propertyDescriptor.getDisplayName());
		
		// If you set display name afterwards, it should keep whatever is set, even camel cased
		propertyDescriptor.setDisplayName("multiWordProperty");
		assertEquals("display name", "multiWordProperty", propertyDescriptor.getDisplayName());
	}
*/

	@Test public void testIsDate() throws Exception
	{
		java.beans.PropertyDescriptor realDescriptor = new java.beans.PropertyDescriptor("date",
			Foo.class);
		TynamoPropertyDescriptor propertyDescriptor = new TynamoPropertyDescriptorImpl(Foo.class, Date.class);
		assertTrue(propertyDescriptor.isDate());
	}

	@Test public void testClone() throws Exception
	{
		TynamoPropertyDescriptorImpl descriptor1 = new TynamoPropertyDescriptorImpl(Foo.class, "foo", String.class);
		TynamoPropertyDescriptorImpl descriptor2 = (TynamoPropertyDescriptorImpl) descriptor1.clone();
		assertEquals("foo", descriptor2.getName());
	}

	@Test public void testCloneWidthExtensions() throws Exception
	{
		String testExtension = "testExtension";
		DescriptorExtension descriptorExtension = new DescriptorExtension()
		{
		};

		TynamoPropertyDescriptorImpl descriptor1 = new TynamoPropertyDescriptorImpl(Foo.class, "foo", String.class);
		descriptor1.addExtension(testExtension, descriptorExtension);

		TynamoPropertyDescriptorImpl descriptor2 = (TynamoPropertyDescriptorImpl) descriptor1.clone();

		assertTrue(descriptor2.supportsExtension(testExtension));
		assertEquals(descriptorExtension, descriptor2.getExtension(testExtension));
	}

	@Test public void testCloneWidthExtensions2() throws Exception
	{
		String testExtension = "testExtension";
		DescriptorExtension descriptorExtension = new DescriptorExtension()
		{
			private final Logger logger = LoggerFactory.getLogger(DescriptorExtension.class);

			private String name;

			private Class propertyType;

			private Class beanType;

			private TynamoPropertyDescriptor propertyDescriptor = null;

			private boolean hidden = true;

			private boolean searchable = true;

			@Override
			public Object clone()
			{
				return new Object();
			}

			public void copyFrom(Descriptor descriptor)
			{

				try
				{
					BeanUtils.copyProperties(this,
						(ObjectReferenceDescriptor) descriptor);
				} catch (IllegalAccessException e)
				{
					logger.error(e.getMessage());
					e.printStackTrace();
				} catch (InvocationTargetException e)
				{
					logger.error(e.getMessage());
					e.printStackTrace();
				} catch (Exception e)
				{
					logger.error(e.toString());
					e.printStackTrace();
				}
			}

			/**
			 * Interface Implementation
			 */

			public String findAddExpression()
			{
				return getExpression("");
			}

			public String findRemoveExpression()
			{
				return getExpression("");
			}

			public String getExpression(String method)
			{
				return getName();
			}

			public String getName()
			{
				return name;
			}

			public void setName(String name)
			{
				this.name = name;
			}

			public Class getBeanType()
			{
				return beanType;
			}

			public void setBeanType(Class beanType)
			{
				this.beanType = beanType;
			}

			public Class getPropertyType()
			{
				return propertyType;
			}

			public void setPropertyType(Class propertyType)
			{
				this.propertyType = propertyType;
			}

			public boolean isSearchable()
			{
				return searchable;
			}

			public void setSearchable(boolean searchable)
			{
				this.searchable = searchable;
			}

			public boolean isHidden()
			{
				return hidden;
			}

			public void setHidden(boolean hidden)
			{
				this.hidden = hidden;
			}

			public TynamoPropertyDescriptor getPropertyDescriptor()
			{
				return propertyDescriptor;
			}

			public void setPropertyDescriptor(
				TynamoPropertyDescriptor propertyDescriptor)
			{
				this.propertyDescriptor = propertyDescriptor;
			}
		};

		TynamoPropertyDescriptorImpl descriptor1 = new TynamoPropertyDescriptorImpl(
			Foo.class, "foo", String.class);
		descriptor1.addExtension(testExtension, descriptorExtension);

		TynamoPropertyDescriptorImpl descriptor2 = (TynamoPropertyDescriptorImpl) descriptor1
			.clone();

		assertTrue(descriptor2.supportsExtension(testExtension));
		assertEquals(descriptorExtension, descriptor2
			.getExtension(testExtension));
	}

}
