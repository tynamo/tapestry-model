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
package org.tynamo.component;

import ognl.Ognl;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.jmock.Mock;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.page.PageResolver;
import org.tynamo.test.Baz;
import org.tynamo.test.Bing;
import org.tynamo.test.Foo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class EditCollectionTest extends ComponentTest
{

	Foo foo;
	Baz baz1;
	Baz baz2;
	Bing bing1;
	Bing bing2;

	Mock pageResolverMock = new Mock(PageResolver.class);

	public void setUp()
	{
		foo = new Foo();

		baz1 = new Baz();
		baz1.setDescription("baz 1");
		baz2 = new Baz();
		baz2.setDescription("baz 2");
		foo.getBazzes().add(baz1);
		foo.getBazzes().add(baz2);

		bing1 = new Bing();
		bing1.setDescription("bing 1");
		bing2 = new Bing();
		bing2.setDescription("bing 2");
		foo.getBings().add(bing1);
		foo.getBings().add(bing2);
	}

	public void testOgnl() throws Exception
	{
		EditCollection editCollection = buildEditCollection("bings", Bing.class, null);
		Ognl.getValue("collectionDescriptor.childRelationship", editCollection);
	}

	public void testBuildSelectedList() throws Exception
	{
		EditCollection editCollection = buildEditCollection("bazzes", Baz.class, foo.getBazzes());
		editCollection.setSelected(editCollection.buildSelectedList());
		assertEquals("2 in list", 2, editCollection.getSelected().size());
		Boolean toBeDeleted = (Boolean) editCollection.getSelected().get(1);
		assertFalse("not to be deleted", toBeDeleted.booleanValue());
	}

	public void testRemove() throws Exception
	{
		EditCollection editCollection = buildEditCollection("bazzes", Baz.class, foo.getBazzes());
		ArrayList<Boolean> deletedList = new ArrayList<Boolean>();
		deletedList.add(true);
		deletedList.add(false);

		// dunno what order they're in really
		Iterator bazIterator = foo.getBazzes().iterator();
		baz1 = (Baz) bazIterator.next();
		baz2 = (Baz) bazIterator.next();
		editCollection.setSelected(deletedList);
		editCollection.remove();
		//System.out.println("size of collection: " + foo.getBazzes().size());
		assertFalse("baz1 removed", foo.getBazzes().contains(baz1));
		assertTrue("baz2 not removed", foo.getBazzes().contains(baz2));
	}

	public void testMove() throws Exception
	{
		EditCollection editCollection = buildEditCollection("bings", Bing.class, foo.getBings());
		ArrayList<Boolean> selectedList = new ArrayList<Boolean>();
		selectedList.add(false);
		selectedList.add(true);
		editCollection.setSelected(selectedList);
		editCollection.moveUp();

		assertEquals("still 2", 2, foo.getBings().size());
		assertEquals("bing2 moved up", bing2, foo.getBings().get(0));

		selectedList.set(0, true);
		selectedList.set(1, false);
		editCollection.moveDown();
		assertEquals("still 2", 2, foo.getBings().size());
		assertEquals("bing2 moved down", bing2, foo.getBings().get(1));
	}

	public void testSortMode() throws Exception
	{
		EditCollection editCollection = buildEditCollection("bings", Bing.class, new ArrayList());
		assertEquals("user sortable", SortMode.USER, editCollection.getSortMode());
	}

	public void testGetSelectionModel() throws Exception
	{
		EditCollection editCollection = buildEditCollection("bings", Bing.class, new ArrayList());
		Bing bing1 = new Bing();
		editCollection.getCollectionDescriptor().setChildRelationship(true);
		editCollection.getCollection().add(bing1);

		TrailsClassDescriptor classDescriptor = new TrailsClassDescriptor(Bing.class);
		classDescriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Foo.class, Bing.class));
		descriptorServiceMock.expects(once()).method("getClassDescriptor")
				.with(eq(Bing.class)).will(returnValue(classDescriptor));

		IPropertySelectionModel selectionModel = editCollection.getSelectionModel();
		assertEquals("has 1", 1, selectionModel.getOptionCount());
		assertEquals("is bing1", bing1, selectionModel.getOption(0));

	}

	private EditCollection buildEditCollection(String property, Class elementType, Collection collection)
	{
		CollectionDescriptor collectionDescriptor = new CollectionDescriptor(Foo.class, property, Set.class);
		collectionDescriptor.setElementType(elementType);
		collectionDescriptor.setChildRelationship(true);

		return (EditCollection) creator.newInstance(EditCollection.class,
				new Object[]{
						"persistenceService", persistenceMock.proxy(),
						"descriptorService", descriptorServiceMock.proxy(),
						"pageResolver", pageResolverMock.proxy(),
						"collectionDescriptor", collectionDescriptor,
						"collection", collection,
						"model", foo
				});
	}
}
