/*
 * Created on Feb 28, 2005
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
package org.trails.callback;

import java.util.List;
import java.util.Set;

import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
import org.trails.component.ComponentTest;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.page.EditPage;
import org.trails.persistence.PersistenceService;
import org.trails.test.Baz;
import org.trails.test.Bing;
import org.trails.test.Foo;

public class CollectionCallbackTest extends ComponentTest
{

	CollectionCallback callBack;
	String pageName = "fooAdd";
	Foo foo = new Foo();
	Baz baz = new Baz();
	CollectionDescriptor collectionDescriptor = new CollectionDescriptor(Foo.class, "bazzes", Set.class);

	public void setUp() throws Exception
	{
		collectionDescriptor.setElementType(Baz.class);
		callBack = new CollectionCallback(pageName, foo, collectionDescriptor);
	}

	public void testCallback()
	{
		EditPage addPage = (EditPage) creator.newInstance(EditPage.class);

		Mock cycleMock = new Mock(IRequestCycle.class);
		cycleMock.expects(once()).method("getPage").with(eq(pageName)).will(returnValue(addPage));
		cycleMock.expects(once()).method("activate").with(same(addPage));
		callBack.performCallback((IRequestCycle) cycleMock.proxy());


	}

	public void testSave() throws Exception
	{
		persistenceMock.expects(once()).method("save").with(eq(foo));
		//persistenceMock.expects(once()).method("reload").with(eq(foo)).will(returnValue(foo));
		callBack.save((PersistenceService) persistenceMock.proxy(), baz);
		assertEquals("1 baz", 1, foo.getBazzes().size());
		assertEquals(baz, foo.getBazzes().iterator().next());
	}

		public void testSaveIndexedList() throws Exception
	{
		CollectionDescriptor bingsCollectionDescriptor = new CollectionDescriptor(Foo.class, "bings", List.class);
		bingsCollectionDescriptor.setChildRelationship(true);
		bingsCollectionDescriptor.setElementType(Bing.class);
		CollectionCallback bingCallBack = new CollectionCallback(pageName, foo, bingsCollectionDescriptor);
		Bing bing = new Bing();

		persistenceMock.expects(atLeastOnce()).method("save").with(eq(foo));
		bingCallBack.save((PersistenceService) persistenceMock.proxy(), bing);

		assertEquals("1 bing", 1, foo.getBings().size());
		assertEquals(bing, foo.getBings().iterator().next());

		bingCallBack.save((PersistenceService) persistenceMock.proxy(), bing);
		assertEquals("if I'm saving the same object it should still be only one bing.", 1, foo.getBings().size());
	}

	public void testRemove() throws Exception
	{
		persistenceMock.expects(once()).method("save").with(eq(foo));
		foo.getBazzes().add(baz);
		callBack.remove((PersistenceService) persistenceMock.proxy(), baz);
		assertEquals("0 bazzes", 0, foo.getBazzes().size());
	}
}
