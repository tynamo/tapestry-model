package org.trails.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.jmock.cglib.Mock;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.trails.descriptor.DescriptorService;
import org.trails.i18n.DefaultTrailsResourceBundleMessageSource;
import org.trails.page.ListPage;
import org.trails.page.PageResolver;
import org.trails.page.SearchPage;
import org.trails.page.TrailsPage;
import org.trails.test.Foo;

public class SearchLinkTest extends ComponentTest
{

    public SearchLinkTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public void testClick()
    {
        DescriptorService descriptorService = (DescriptorService)descriptorServiceMock.proxy();
        DefaultTrailsResourceBundleMessageSource messageSource = new DefaultTrailsResourceBundleMessageSource();
        ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
        springMessageSource.setBasename("messages");
        messageSource.setMessageSource(springMessageSource);
    	
        Mock cycleMock = new Mock(IRequestCycle.class);
        Mock pageResolverMock = new Mock(PageResolver.class);
        
        
        SearchPage searchPage = buildTrailsPage(SearchPage.class);
        SearchLink searchLink = (SearchLink)creator.newInstance(SearchLink.class, 
                new Object[] {"descriptorService", descriptorServiceMock.proxy(), 
        	"resourceBundleMessageSource", messageSource,
        	"pageResolver", pageResolverMock.proxy()});
        searchLink.setTypeName(Foo.class.getName());
        
        // Pretend Foo has a custom page
        pageResolverMock.expects(once()).method("resolvePage").with(
        		isA(IRequestCycle.class), 
        		eq(Foo.class.getName()),
        		eq(TrailsPage.PageType.SEARCH)).will(returnValue(searchPage));
        cycleMock.expects(once()).method("activate").with(eq(searchPage));
        searchLink.click((IRequestCycle)cycleMock.proxy());
        assertNotNull(searchPage.getExampleModel());
        assertTrue(searchPage.getExampleModel() instanceof Foo);
    }
}
