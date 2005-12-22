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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.IRequestCycle;

import org.jmock.cglib.Mock;
import org.jmock.cglib.MockObjectTestCase;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.DescriptorService;
import org.trails.i18n.DefaultTrailsResourceBundleMessageSource;
import org.trails.page.ListPage;
import org.trails.persistence.PersistenceService;
import org.trails.test.BlogEntry;
import org.trails.test.Foo;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ListAllLinkTest extends ComponentTest
{
    ListAllLink listLink;
    public void setUp()
    {
        
        DescriptorService descriptorService = (DescriptorService)descriptorServiceMock.proxy();
        DefaultTrailsResourceBundleMessageSource messageSource = new DefaultTrailsResourceBundleMessageSource();
        ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
        springMessageSource.setBasename("messages");
        messageSource.setMessageSource(springMessageSource);
        listLink = (ListAllLink) creator.newInstance(ListAllLink.class, 
                new Object[] {"descriptorService", descriptorService,
        					  "resourceBundleMessageSource", messageSource});
        listLink.setTypeName(Foo.class.getName());
        
    }
    
    public void testGetLinkText() throws Exception
    {
 
        //listLink.setDescriptorService(descriptorService);
        IClassDescriptor descriptor = new TrailsClassDescriptor(BlogEntry.class, "BlogEntry");
        descriptorServiceMock.expects(once()).method("getClassDescriptor").will(returnValue(descriptor));
        assertEquals("List Blog Entries", listLink.getLinkText());
        
    }

    public void testClick()
    {
        ListPage listPage = buildTrailsPage(ListPage.class);
        Mock cycleMock = new Mock(IRequestCycle.class);       

        List instances = new ArrayList();
        persistenceMock.expects(once()).method("getAllInstances").withAnyArguments()
                .will(returnValue(instances));

        // Pretend Foo has a custom page
        cycleMock.expects(once()).method("getPage").with(eq("FooList")).will(returnValue(
                listPage));
        cycleMock.expects(once()).method("activate").with(eq(listPage));
        //cycleMock.expects(atLeastOnce()).method("activate").with(eq(Foo.class), isA(IRequestCycle.class));
        listLink.click((IRequestCycle) cycleMock.proxy());
        assertEquals("instances set", instances, listPage.getInstances());
        assertEquals("got right type", listPage.getTypeName(),
            listLink.getTypeName());
        cycleMock.verify();
    }
}
