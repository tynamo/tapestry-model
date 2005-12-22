package org.trails.callback;

import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
import org.trails.component.ComponentTest;
import org.trails.page.SearchPage;
import org.trails.test.Foo;

import junit.framework.TestCase;

public class SearchCallbackTest extends ComponentTest
{

    public SearchCallbackTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    String pageName = "FooSearch";

    public void testPerformCallback()
    {
        SearchPage searchPage = buildTrailsPage(SearchPage.class);
        Object model = new Object();
        Mock cycleMock = new Mock(IRequestCycle.class);
        cycleMock.expects(once()).method("getPage").with(eq(pageName)).will(returnValue(searchPage));
        cycleMock.expects(once()).method("activate").with(same(searchPage));
        
        SearchCallback callBack = new SearchCallback(pageName, model);
        callBack.performCallback((IRequestCycle)cycleMock.proxy());
        assertEquals(model, searchPage.getExampleModel());
        cycleMock.verify();        
    }

}
