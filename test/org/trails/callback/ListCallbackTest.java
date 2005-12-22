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

import java.util.ArrayList;

import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
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
public class ListCallbackTest extends ComponentTest
{

    public void testCallBack()
    {
        
        persistenceMock.expects(once()).method("getAllInstances").with(same(Foo.class)).will(returnValue(new ArrayList()));                
        ListPage listPage = buildTrailsPage(ListPage.class);
        String pageName = "fooList";
        Object model = new Object();
        Mock cycleMock = new Mock(IRequestCycle.class);
        cycleMock.expects(once()).method("getPage").with(eq(pageName)).will(returnValue(listPage));
        cycleMock.expects(once()).method("activate").with(same(listPage));
        
        ListCallback callBack = new ListCallback(pageName, Foo.class.getName());
        callBack.performCallback((IRequestCycle)cycleMock.proxy());
        cycleMock.verify();
    }
}
