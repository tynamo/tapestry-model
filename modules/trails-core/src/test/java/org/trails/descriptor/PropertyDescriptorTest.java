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

import java.util.Date;

import junit.framework.TestCase;
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

}
