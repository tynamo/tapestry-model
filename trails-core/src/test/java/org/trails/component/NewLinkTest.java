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
import org.springframework.context.support.ResourceBundleMessageSource;
import org.trails.callback.TrailsCallback;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.i18n.DefaultTrailsResourceBundleMessageSource;
import org.trails.page.EditPage;
import org.trails.page.HomePage;
import org.trails.page.ListPage;
import org.trails.page.PageResolver;
import org.trails.page.TrailsPage;
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
    Mock cycleMock = new Mock(IRequestCycle.class);
    Mock pageMock = new Mock(IPage.class);
    EditPage editPage;


    public void testClick()
    {
    	ListPage listPage = buildTrailsPage(ListPage.class);
        listPage.setTypeName(Foo.class.getName());
        listPage.setPageName("list");

        buildNewLink(listPage);
        cycleMock.verify();
        assertTrue("model is a foo", editPage.getModel() instanceof Foo);
    }

	private void buildNewLink(TrailsPage listPage)
	{
		// Pretend Foo has a custom page
//        cycleMock.expects(once()).method("getPage").with(eq("FooEdit")).will(returnValue(
//                editPage));
        cycleMock.expects(atLeastOnce()).method("activate").with(same(editPage));
		pageResolverMock.expects(once()).method("resolvePage")
			.with(isA(IRequestCycle.class), eq(Foo.class.getName()), eq(TrailsPage.PageType.EDIT))
			.will(returnValue(editPage));
        //newLink = (NewLink) creator.newInstance(NewLink.class);
        newLink.setTypeName(Foo.class.getName());
        newLink.setPage(listPage);
        newLink.click((IRequestCycle) cycleMock.proxy());
	}

    public void testFromHomePage() throws Exception
    {
        HomePage homePage = (HomePage) buildTrailsPage(HomePage.class);
        homePage.pushCallback();
        buildNewLink(homePage);
        assertTrue("Default callback", homePage.getCallbackStack().getStack().pop() instanceof TrailsCallback);
        assertTrue("Now is empty", homePage.getCallbackStack().getStack().empty());
    }

    Mock pageResolverMock;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        DescriptorService descriptorService = (DescriptorService)descriptorServiceMock.proxy();
        DefaultTrailsResourceBundleMessageSource messageSource = new DefaultTrailsResourceBundleMessageSource();
        ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
        springMessageSource.setBasename("messages");
        messageSource.setMessageSource(springMessageSource);
        editPage = buildTrailsPage(EditPage.class);
        pageResolverMock = new Mock(PageResolver.class);

        newLink = (NewLink) creator.newInstance(NewLink.class,
                new Object[] {
                    "descriptorService", descriptorService,
                    "resourceBundleMessageSource", messageSource,
                    "pageResolver", pageResolverMock.proxy()
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
