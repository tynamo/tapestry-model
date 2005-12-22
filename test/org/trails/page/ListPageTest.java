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
import java.util.Stack;

import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
import org.trails.callback.ListCallback;
import org.trails.component.ComponentTest;
import org.trails.page.ListPage;
import org.trails.persistence.PersistenceService;
import org.trails.test.Foo;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ListPageTest extends ComponentTest
{
    public static final String PAGE_NAME = "fooList";
    ListPage listPage;
    Mock cycleMock;
    
    List stuff = new ArrayList();
    
    public void setUp()
    {
        
        listPage = (ListPage) buildTrailsPage(ListPage.class);
        listPage.setPageName(PAGE_NAME);

        cycleMock = new Mock(IRequestCycle.class);
        
        
    }
    
    public void testExternalPage()
    {
        persistenceMock.expects(once()).method("getAllInstances")
        	.with(same(Foo.class)).will(returnValue(stuff));        
        cycleMock.expects(once()).method("activate").with(eq(listPage));
        listPage.activateExternalPage(new Object[] { Foo.class },
            (IRequestCycle) cycleMock.proxy());
        persistenceMock.verify();
        assertEquals("got instances", stuff, listPage.getInstances());
        cycleMock.verify();
    }
    
    public void testLoadInstances()
    {
        persistenceMock.expects(once()).method("getAllInstances")
        	.with(same(Foo.class)).will(returnValue(stuff));        
        listPage.setTypeName(Foo.class.getName());
        listPage.loadInstances();
        persistenceMock.verify();
    }
    
    public void testPushCallback()
    {
        listPage.setTypeName(Foo.class.getName());
        
        listPage.pushCallback();
        ListCallback listCallback = (ListCallback)listPage.getCallbackStack().pop();
        
        assertEquals(PAGE_NAME, listCallback.getPageName());
        assertEquals(Foo.class.getName(), listCallback.getTypeName());
    }
}
