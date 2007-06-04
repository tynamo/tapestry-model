/*
 * Created on Feb 27, 2005
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

import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
import org.trails.component.ComponentTest;
import org.trails.page.EditPage;
import org.trails.test.Foo;

/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class EditCallbackTest extends ComponentTest
{
	Foo foo;
	EditCallback editCallback;
	EditCallback editCallback2;
	Foo foo2;

	public void setUp() throws Exception
	{
		foo = new Foo();
		editCallback = new EditCallback("FooEdit", foo);
		editCallback2 = new EditCallback("FooEdit", foo);
		foo2 = new Foo();
	}

	public void testCallBack()
	{
		EditPage editPage = (EditPage) creator.newInstance(EditPage.class);
		String pageName = "fooPage";
		Object model = new Object();
		Mock cycleMock = new Mock(IRequestCycle.class);
		cycleMock.expects(once()).method("getPage").with(eq(pageName)).will(returnValue(editPage));
		cycleMock.expects(once()).method("activate").with(same(editPage));
		EditCallback callBack = new EditCallback(pageName, model);
		callBack.performCallback((IRequestCycle) cycleMock.proxy());
	}

	public void testEquals() throws Exception
	{
		assertEquals(editCallback, editCallback2);

		EditCallback editCallback3 = new EditCallback("FooEdit", foo2);
		assertFalse(editCallback3.equals(editCallback));
		assertFalse(editCallback.equals(editCallback3));
		editCallback3 = new EditCallback("NotFooEdit", foo);
		assertFalse(editCallback3.equals(editCallback));
		assertFalse(editCallback.equals(editCallback3));
//    	CollectionCallback collectionCallback = new CollectionCallback("FooEdit", foo);
//    	assertFalse(editCallback.equals(collectionCallback));
//    	assertFalse(collectionCallback.equals(editCallback));
	}

	public void testShouldReplace() throws Exception
	{
		EditCallback editCallback = new EditCallback("FooEdit", foo);
		EditCallback editCallback2 = new EditCallback("FooEdit", foo);
		assertTrue(editCallback.shouldReplace(editCallback2));
		editCallback2 = new EditCallback("FooEdit", foo2);
		assertFalse(editCallback.shouldReplace(editCallback2));
		editCallback2 = new EditCallback("FooEdit", foo2, true);
		assertTrue(editCallback.shouldReplace(editCallback2));

	}
}
