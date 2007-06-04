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
package org.trails.descriptor;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import junit.framework.TestCase;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.test.Foo;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PropertyDescriptorTest extends TestCase
{
	public void testIsNumeric() throws Exception
	{

		IPropertyDescriptor propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, Double.class);
		assertTrue(propertyDescriptor.isNumeric());
	}

	public void testIsBoolean() throws Exception
	{

		IPropertyDescriptor propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, boolean.class);
		assertTrue(propertyDescriptor.isBoolean());
	}

	public void testDisplayName() throws Exception
	{
		IPropertyDescriptor propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, String.class);
		propertyDescriptor.setDisplayName("multiWordProperty");
		assertEquals("display name", "Multi Word Property",
			propertyDescriptor.getDisplayName());
	}

	public void testIsDate() throws Exception
	{
		java.beans.PropertyDescriptor realDescriptor = new java.beans.PropertyDescriptor("date",
			Foo.class);
		IPropertyDescriptor propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, Date.class);
		assertTrue(propertyDescriptor.isDate());
	}

	public void testClone() throws Exception
	{
		TrailsPropertyDescriptor descriptor1 = new TrailsPropertyDescriptor(Foo.class, "foo", String.class);
		TrailsPropertyDescriptor descriptor2 = (TrailsPropertyDescriptor) descriptor1.clone();
		assertEquals("foo", descriptor2.getName());
	}

	public void testCloneWidthExtensions() throws Exception
	{
		String testExtension = "testExtension";
		IDescriptorExtension descriptorExtension = new IDescriptorExtension()
		{
		};

		TrailsPropertyDescriptor descriptor1 = new TrailsPropertyDescriptor(Foo.class, "foo", String.class);
		descriptor1.addExtension(testExtension, descriptorExtension);

		TrailsPropertyDescriptor descriptor2 = (TrailsPropertyDescriptor) descriptor1.clone();

		assertTrue(descriptor2.supportsExtension(testExtension));
		assertEquals(descriptorExtension, descriptor2.getExtension(testExtension));
	}

	public void testCloneWidthExtensions2() throws Exception
	{
		String testExtension = "testExtension";
		IDescriptorExtension descriptorExtension = new IDescriptorExtension()
		{
			protected final Log LOG = LogFactory
				.getLog(IDescriptorExtension.class);

			private String name;

			private Class propertyType;

			private Class beanType;

			private IPropertyDescriptor propertyDescriptor = null;

			private boolean hidden = true;

			private boolean searchable = true;

			@Override
			public Object clone()
			{
				return new Object();
			}

			public void copyFrom(IDescriptor descriptor)
			{

				try
				{
					BeanUtils.copyProperties(this,
						(ObjectReferenceDescriptor) descriptor);
				} catch (IllegalAccessException e)
				{
					LOG.error(e.getMessage());
					e.printStackTrace();
				} catch (InvocationTargetException e)
				{
					LOG.error(e.getMessage());
					e.printStackTrace();
				} catch (Exception e)
				{
					LOG.error(e.toString());
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

			/**
			 * @see org.trails.descriptor.PropertyDescriptor#getPropertyType()
			 */
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

			public IPropertyDescriptor getPropertyDescriptor()
			{
				return propertyDescriptor;
			}

			public void setPropertyDescriptor(
				IPropertyDescriptor propertyDescriptor)
			{
				this.propertyDescriptor = propertyDescriptor;
			}
		};

		TrailsPropertyDescriptor descriptor1 = new TrailsPropertyDescriptor(
			Foo.class, "foo", String.class);
		descriptor1.addExtension(testExtension, descriptorExtension);

		TrailsPropertyDescriptor descriptor2 = (TrailsPropertyDescriptor) descriptor1
			.clone();

		assertTrue(descriptor2.supportsExtension(testExtension));
		assertEquals(descriptorExtension, descriptor2
			.getExtension(testExtension));
	}

}
