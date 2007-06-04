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
package org.trails.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.jmock.Mock;
import org.trails.callback.ListCallback;
import org.trails.component.ComponentTest;
import org.trails.test.Foo;


public class ListPageTest extends ComponentTest
{

	public static final String PAGE_NAME = "fooList";
	ListPage listPage;
	Mock cycleMock;

	List<Foo> stuff = new ArrayList<Foo>();

	public void setUp()
	{

		listPage = (ListPage) buildTrailsPage(ListPage.class);
		listPage.setPageName(PAGE_NAME);

		cycleMock = new Mock(IRequestCycle.class);
		listPage.attach(null, (IRequestCycle) cycleMock.proxy());

	}

	public void testPageBeginRender() throws Exception
	{

		PageEvent pageEvent = new PageEvent(listPage, (IRequestCycle) cycleMock.proxy());
		listPage.setType(Foo.class);
		listPage.pageBeginRender(pageEvent);
		assertEquals(1, listPage.getCallbackStack().getStack().size());
	}

	public void testExternalPage()
	{

		persistenceMock.expects(once()).method("getAllInstances").with(eq(Foo.class)).will(returnValue(stuff));
		listPage.activateExternalPage(new Object[]{Foo.class}, (IRequestCycle) cycleMock.proxy());
		assertNotNull(listPage.getType());
		assertNotNull(listPage.getInstances());
		assertEquals(stuff, listPage.getInstances());
	}


	public void testPushCallback()
	{
		listPage.setTypeName(Foo.class.getName());
		listPage.setType(Foo.class);
		listPage.pushCallback();
		ListCallback listCallback = (ListCallback) listPage.getCallbackStack().getStack().pop();

		assertEquals(PAGE_NAME, listCallback.getPageName());
/*
        assertEquals(Foo.class.getName(), listCallback.getTypeName());
        assertEquals(criteria, listCallback.getCriteria());
*/
	}
}
