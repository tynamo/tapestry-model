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

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
import org.trails.page.PageResolver;
import org.trails.page.TrailsPage;
import org.trails.test.Foo;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class EditLinkTest extends ComponentTest
{
	public void testGetPageName()
	{
		Mock pageMock = new Mock(IPage.class);
		Mock pageResolverMock = new Mock(PageResolver.class);
		Mock cycleMock = new Mock(IRequestCycle.class);
		pageResolverMock.expects(once()).method("resolvePage")
			.with(isA(IRequestCycle.class), eq(Foo.class.getName()), eq(TrailsPage.PageType.Edit))
			.will(returnValue(pageMock.proxy()));
		pageMock.expects(once()).method("getPageName").will(returnValue("FooEdit"));
		pageMock.expects(once()).method("getRequestCycle").will(returnValue(cycleMock.proxy()));
		EditLink editLink = (EditLink) creator.newInstance(EditLink.class, new Object[]{"pageResolver", pageResolverMock.proxy()});
		editLink.setPage((IPage) pageMock.proxy());
		editLink.setModel(new Foo());
		assertEquals("FooEdit", editLink.getEditPageName());
	}
}
