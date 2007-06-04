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
package org.trails.component;

import java.util.ArrayList;

import junit.framework.TestCase;
import org.trails.test.Foo;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class IdentifierSelectionModelTest extends TestCase
{
	IdentifierSelectionModel selectionModel;
	IdentifierSelectionModel nullableSelectionModel;
	IdentifierSelectionModel labelSelectionModel;

	ArrayList foos = new ArrayList();

	public void testGetValue() throws Exception
	{
		assertEquals("right value", "2", selectionModel.getValue(1));
		assertEquals("none value", IdentifierSelectionModel.DEFAULT_NONE_VALUE,
			nullableSelectionModel.getValue(0));
		assertEquals("1 value", "1",
			nullableSelectionModel.getValue(1));
	}

	public void testSetAllowNone() throws Exception
	{
		nullableSelectionModel.setAllowNone(foos, true);
		assertEquals(3, nullableSelectionModel.getOptionCount());
		nullableSelectionModel.setAllowNone(foos, true);
		assertEquals(3, nullableSelectionModel.getOptionCount());
	}

	public void testNoneLabelValue() throws Exception
	{
		nullableSelectionModel.setNoneLabel("Any");
		assertEquals("Any", nullableSelectionModel.getLabel(0));
	}

	public void testGetLabel() throws Exception
	{
		assertEquals("right label", "mom", selectionModel.getLabel(1));
		assertEquals("none value", IdentifierSelectionModel.DEFAULT_NONE_LABEL,
			nullableSelectionModel.getLabel(0));
		assertEquals("none value", "howdy",
			nullableSelectionModel.getLabel(1));
		assertEquals("property label 2", "mother", labelSelectionModel.getLabel(2));
	}

	public void testTranslateValue() throws Exception
	{
		Foo foo = (Foo) selectionModel.translateValue("2");
		assertEquals("got right foo", foo, foos.get(1));
		assertEquals("should be null", null, nullableSelectionModel.translateValue(
			IdentifierSelectionModel.DEFAULT_NONE_VALUE));
		assertEquals("correct foo", foo, nullableSelectionModel.translateValue("2"));
		assertEquals("translate foo by label property", foo, labelSelectionModel.translateValue("2"));
		assertNull("dont blow an exception", selectionModel.translateValue(null));
		assertNull("dont blow an exception", selectionModel.translateValue("a value that doesn't exist"));
	}

	public void testGetOptionCount() throws Exception
	{
		assertEquals(2, selectionModel.getOptionCount());
		assertEquals(3, nullableSelectionModel.getOptionCount());
	}

	/**
	 *
	 */
	public void setUp()
	{
		Foo foo = new Foo();
		foo.setId(new Integer(1));
		foo.setName("howdy");
		foo.setHidden("hello");

		Foo foo2 = new Foo();
		foo2.setId(new Integer(2));
		foo2.setName("mom");
		foo2.setHidden("mother");
		foos.add(foo);
		foos.add(foo2);

		selectionModel = new IdentifierSelectionModel(foos, "id");
		nullableSelectionModel = new IdentifierSelectionModel(foos, "id", true);
		labelSelectionModel = new IdentifierSelectionModel(foos, "id", true);
		labelSelectionModel.setLabelProperty("hidden");
	}
}
