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

import java.util.Stack;

import org.trails.TrailsRuntimeException;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.apache.tapestry.event.PageEvent;

import org.jmock.Mock;
import org.trails.callback.ListCallback;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.hibernate.HasAssignedIdentifier;
import org.trails.page.EditPage;
import org.trails.page.ListPage;
import org.trails.persistence.PersistenceService;
import org.trails.test.Bar;
import org.trails.test.Foo;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditLinkTest extends ComponentTest
{
    EditPage editPage;
    Mock cycleMock;
    Foo foo;
    EditLink editLink;
    ListPage listPage;
    
    public void setUp() throws Exception
    {
        
        editPage = (EditPage) creator.newInstance(EditPage.class);
        cycleMock = new Mock(IRequestCycle.class);
        Mock pageMock = new Mock(IPage.class);
        foo = new Foo();
        foo.setId(new Integer(2));
        ((HasAssignedIdentifier)foo).onSave();
        
 
        editLink = (EditLink) creator.newInstance(EditLink.class,
            new Object[] {"persistenceService", persistenceMock.proxy()});
        
        
        listPage = buildTrailsPage(ListPage.class);
        listPage.setPageName("listPage");
        editLink.setPage(listPage);
        //editLink.setTypeName(Foo.class.getName());
        editLink.setModel(foo);
        
    }
    
    public void testClick() throws Exception
    {
        listPage.setTypeName(Foo.class.getName());
        persistenceMock.expects(once()).method("reattach").with(eq(foo));

        // Pretend Foo has a custom page
        cycleMock.expects(atLeastOnce()).method("getPage").with(eq("FooEdit")).will(returnValue(
                editPage));
        cycleMock.expects(atLeastOnce()).method("activate").with(same(editPage));

        editLink.setModel(foo);

        
        editLink.click((IRequestCycle) cycleMock.proxy());

        // check so see list page is on stack
        assertTrue(listPage.getCallbackStack().pop() instanceof ListCallback);
        cycleMock.verify();
        persistenceMock.verify();

    }
    
    public void testWithDefaultPage() throws Exception
    {
        // Now pretend Bar doesn't have a custom page
        listPage.setTypeName(Foo.class.getName());
        Bar bar = new Bar();
        bar.setId(new Integer(2));

        persistenceMock.expects(once()).method("reattach").with(eq(bar));

        editLink.setModel(bar);
        
        cycleMock.expects(atLeastOnce()).method("getPage").with(eq("BarEdit")).will(throwException(
                new PageNotFoundException("Not found")));
        cycleMock.expects(atLeastOnce()).method("getPage").with(eq("DefaultEdit"))
                 .will(returnValue(editPage));
        cycleMock.expects(atLeastOnce()).method("activate").with(same(editPage));
        
        editLink.click((IRequestCycle) cycleMock.proxy());
    }
}
