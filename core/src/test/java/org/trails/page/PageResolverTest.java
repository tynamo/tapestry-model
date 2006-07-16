package org.trails.page;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.trails.test.Foo;

public class PageResolverTest extends MockObjectTestCase
{
	public void testResolve() throws Exception
	{
		PageResolver pageResolver = new DefaultPageResolver();
		Mock cycleMock = new Mock(IRequestCycle.class);
		Mock pageMock = new Mock(IPage.class);
		IPage fooSearchPage = (IPage)pageMock.proxy();
		IPage defaultEditPage = (IPage)pageMock.proxy();
		cycleMock.expects(once()).method("getPage").with(eq("FooSearch")).will(returnValue(fooSearchPage));
		cycleMock.expects(once()).method("getPage").with(eq("FooEdit")).will(throwException(new PageNotFoundException("blah")));
		cycleMock.expects(once()).method("getPage").with(eq("DefaultEdit")).will(returnValue(defaultEditPage));
		
		assertEquals(fooSearchPage, 
			pageResolver.resolvePage((IRequestCycle)cycleMock.proxy(), Foo.class.getName(), TrailsPage.PageType.SEARCH));
		assertEquals(defaultEditPage,
			pageResolver.resolvePage((IRequestCycle)cycleMock.proxy(), Foo.class.getName(), TrailsPage.PageType.EDIT));
		
	}
}
