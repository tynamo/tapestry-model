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

import java.beans.Introspector;
import java.util.Stack;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.trails.callback.ListCallback;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.i18n.DefaultTrailsResourceBundleMessageSource;
import org.trails.page.EditPage;
import org.trails.page.ListPage;
import org.trails.test.BlogEntry;
import org.trails.test.Foo;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewLinkTest extends ComponentTest
{
    NewLink newLink;
    
    public void testClick()
    {
        EditPage editPage = buildTrailsPage(EditPage.class);
        ListPage listPage = buildTrailsPage(ListPage.class);
        listPage.setTypeName(Foo.class.getName());
        listPage.setPageName("list");
        
        
        Mock cycleMock = new Mock(IRequestCycle.class);
        Mock pageMock = new Mock(IPage.class);

        // Pretend Foo has a custom page
        cycleMock.expects(once()).method("getPage").with(eq("FooEdit")).will(returnValue(
                editPage));
        cycleMock.expects(atLeastOnce()).method("activate").with(same(editPage));

        NewLink newLink = (NewLink) creator.newInstance(NewLink.class);
        newLink.setTypeName(Foo.class.getName());
        newLink.setPage(listPage);
        newLink.click((IRequestCycle) cycleMock.proxy());
        assertTrue("list callback on stack", listPage.getCallbackStack().pop() instanceof ListCallback);
        cycleMock.verify();
        assertTrue("model is a foo", editPage.getModel() instanceof Foo);
    }


    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        DescriptorService descriptorService = (DescriptorService)descriptorServiceMock.proxy();
        DefaultTrailsResourceBundleMessageSource messageSource = new DefaultTrailsResourceBundleMessageSource();
        ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
        springMessageSource.setBasename("messages");
        messageSource.setMessageSource(springMessageSource);
        newLink = (NewLink) creator.newInstance(NewLink.class, 
                new Object[] {
                    "descriptorService", descriptorService,
                    "resourceBundleMessageSource", messageSource
                });
    }


    public void testGetLinkText() throws Exception
    {
        newLink.setTypeName(BlogEntry.class.getName());
        IClassDescriptor descriptor = new TrailsClassDescriptor(BlogEntry.class, "BlogEntry");
        descriptorServiceMock.expects(once()).method("getClassDescriptor").will(returnValue(descriptor));  
        assertEquals("New Blog Entry", newLink.getLinkText());
    }
}
