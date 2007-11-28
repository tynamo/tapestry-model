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

import ognl.Ognl;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.jmock.Mock;
import org.trails.callback.CallbackStack;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.page.EditPage;
import org.trails.page.PageResolver;
import org.trails.test.Baz;
import org.trails.test.Bing;
import org.trails.test.Foo;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


public class EditCollectionTest extends ComponentTest
{

	EditPage editPage;

	Foo foo;
	Baz baz1;
	Baz baz2;
	Bing bing1;
	Bing bing2;

	EditCollection editCollection;
	EditPage addPage;
	Mock pageMock = new Mock(IPage.class);
	Mock pageResolverMock = new Mock(PageResolver.class);

	public void setUp()
	{
		editCollection = (EditCollection) creator.newInstance(EditCollection.class,
				new Object[]{
						"persistenceService", persistenceMock.proxy(),
						"descriptorService", descriptorServiceMock.proxy(),
						"callbackStack", callbackStack,
						"pageResolver", pageResolverMock.proxy()
				});
		editPage = buildTrailsPage(EditPage.class);

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

		editPage.setModel(foo);
		editPage.setPageName("fooPage");

		addPage = (EditPage) creator.newInstance(EditPage.class);

		editCollection.setCallbackStack(new CallbackStack());
		editCollection.setPage((IPage) pageMock.proxy());

		editCollection.setCollection(new ArrayList());
	}

	public void testOgnl() throws Exception
	{
		buildCollectionDescriptor("bings", Bing.class);
		Ognl.getValue("collectionDescriptor.childRelationship", editCollection);
	}

	/**
	 * @throws IntrospectionException
	 */
	private void buildCollectionDescriptor(String property, Class elementType)
			throws IntrospectionException
	{
		CollectionDescriptor collectionDescriptor = new CollectionDescriptor(Foo.class, Set.class);
		collectionDescriptor.setName(property);
		collectionDescriptor.setElementType(elementType);
		collectionDescriptor.setChildRelationship(true);
		editCollection.setPropertyDescriptor(collectionDescriptor);
		editCollection.setModel(foo);

	}


	/**
	 * @return
	 */
	protected Mock buildCycleMock(String element)
	{
		// lets say Foo_addBaz has custom page
		Mock cycleMock = new Mock(IRequestCycle.class);
		cycleMock.expects(once()).method("getPage").with(eq(
				element + "Edit")).will(returnValue(addPage));
		cycleMock.expects(atLeastOnce()).method("activate").with(same(addPage));
		cycleMock.expects(atLeastOnce()).method("getPage").with(eq("fooEditPage")).will(
				returnValue(editPage));

		return cycleMock;
	}

	public void testBuildSelectedList() throws Exception
	{

		editCollection.setCollection(foo.getBazzes());
		editCollection.buildSelectedList();
		assertEquals("2 in list", 2, editCollection.getSelected().size());
		Boolean toBeDeleted = (Boolean) editCollection.getSelected().get(1);
		assertFalse("not to be deleted", toBeDeleted.booleanValue());
		editCollection.setCollection(null);
		editCollection.buildSelectedList();
	}

	public void testRemove() throws Exception
	{
		buildCollectionDescriptor("bazzes", Baz.class);

		editCollection.setCollection(foo.getBazzes());
		ArrayList deletedList = new ArrayList();
		deletedList.add(new Boolean(true));
		deletedList.add(new Boolean(false));
		Mock cycleMock = new Mock(IRequestCycle.class);

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
		buildCollectionDescriptor("bings", Bing.class);

		editCollection.setCollection(foo.getBings());
		ArrayList selectedList = new ArrayList();
		selectedList.add(new Boolean(false));
		selectedList.add(new Boolean(true));
		editCollection.setSelected(selectedList);
		Mock cycleMock = new Mock(IRequestCycle.class);
		editCollection.moveUp();

		assertEquals("still 2", 2, foo.getBings().size());
		assertEquals("bing2 moved up", bing2, foo.getBings().get(0));

		selectedList.set(0, new Boolean(true));
		selectedList.set(1, new Boolean(false));
		editCollection.moveDown();
		assertEquals("still 2", 2, foo.getBings().size());
		assertEquals("bing2 moved down", bing2, foo.getBings().get(1));
	}

	public void testSortMode() throws Exception
	{
		buildCollectionDescriptor("bings", Bing.class);
		editCollection.setCollection(new ArrayList());
		assertEquals("user sortable", SortMode.USER, editCollection.getSortMode());
	}

	public void testGetSelectionModel() throws Exception
	{
		buildCollectionDescriptor("bings", Bing.class);
		Bing bing1 = new Bing();
		editCollection.getCollectionDescriptor().setChildRelationship(true);
		editCollection.getCollection().add(bing1);

		IClassDescriptor classDescriptor = new TrailsClassDescriptor(Bing.class);
		classDescriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Foo.class, Bing.class));
		descriptorServiceMock.expects(once()).method("getClassDescriptor")
				.with(eq(Bing.class)).will(returnValue(classDescriptor));

		IPropertySelectionModel selectionModel = editCollection.getSelectionModel();
		assertEquals("has 1", 1, selectionModel.getOptionCount());
		assertEquals("is bing1", bing1, selectionModel.getOption(0));

	}

}
