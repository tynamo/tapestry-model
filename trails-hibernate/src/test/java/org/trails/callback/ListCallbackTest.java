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
import org.hibernate.criterion.DetachedCriteria;
import org.jmock.Mock;
import org.trails.component.ComponentTest;
import org.trails.page.HibernateListPage;
import org.trails.test.Foo;

/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ListCallbackTest extends ComponentTest
{

	DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);

	public void testCallBack()
	{

		HibernateListPage listPage = buildTrailsPage(HibernateListPage.class);
		String pageName = "fooList";
		Object model = new Object();
		Mock cycleMock = new Mock(IRequestCycle.class);
		cycleMock.expects(once()).method("getPage").with(eq(pageName)).will(returnValue(listPage));
		cycleMock.expects(once()).method("activate").with(same(listPage));

		HibernateListCallback callBack = new HibernateListCallback(pageName, Foo.class.getName(), criteria);
		DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
		callBack.setCriteria(criteria);
		callBack.performCallback((IRequestCycle) cycleMock.proxy());
		assertEquals(criteria, listPage.getCriteria());
		cycleMock.verify();
	}

	public void testShouldReplace()
	{
		HibernateListCallback callBack = new HibernateListCallback("FooList", Foo.class.getName(), criteria);
		HibernateListCallback callBack2 = new HibernateListCallback("FooList", Foo.class.getName(), criteria);
		Assert.assertTrue(callBack2.shouldReplace(callBack));
		callBack2 = new HibernateListCallback("Blork", Foo.class.getName(), criteria);
		Assert.assertFalse(callBack2.shouldReplace(callBack));
		EditCallback editCallback = new EditCallback("FooEdit", new Foo());
		Assert.assertFalse(editCallback.shouldReplace(callBack2));

	}
}