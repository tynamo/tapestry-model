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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.hibernate.criterion.DetachedCriteria;
import org.jmock.Mock;
import org.trails.callback.ListCallback;
import org.trails.component.ComponentTest;
import org.trails.testhibernate.Foo;

import java.util.ArrayList;
import java.util.List;

public class HibernateListPageTest extends ComponentTest {

    public static final String PAGE_NAME = "fooList";
    HibernateListPage listPage;
    Mock cycleMock;

    List stuff = new ArrayList();

    public void setUp() {

        listPage = (HibernateListPage) buildTrailsPage(HibernateListPage.class);
        listPage.setPageName(PAGE_NAME);

        cycleMock = new Mock(IRequestCycle.class);
        listPage.attach(null, (IRequestCycle) cycleMock.proxy());

    }

    public void testPageBeginRender() throws Exception {

        PageEvent pageEvent = new PageEvent(listPage, (IRequestCycle) cycleMock.proxy());
        listPage.setCriteria(DetachedCriteria.forClass(Foo.class));
        listPage.pageBeginRender(pageEvent);
        assertEquals(1, listPage.getCallbackStack().getStack().size());
    }

    public void testExternalPage() {
        listPage.activateExternalPage(new Object[]{Foo.class},
                (IRequestCycle) cycleMock.proxy());
        assertNotNull(listPage.getCriteria());
    }


    public void testPushCallback() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
        listPage.setType(Foo.class);
        listPage.setCriteria(criteria);
        listPage.pushCallback();
        ListCallback listCallback = (ListCallback) listPage.getCallbackStack().getStack().pop();

        assertEquals(PAGE_NAME, listCallback.getPageName());
    }
}
