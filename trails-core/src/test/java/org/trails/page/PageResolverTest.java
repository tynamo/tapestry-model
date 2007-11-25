package org.trails.page;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.trails.test.Foo;

public class PageResolverTest extends MockObjectTestCase
{
	public void testResolve() throws Exception
	{
		DefaultPageResolver pageResolver = new DefaultPageResolver();
		pageResolver.setCacheDisabled(true);
		final IRequestCycle cycle = mock(IRequestCycle.class);

		final IPage fooSearchPage = mock(IPage.class);
		final IPage defaultEditPage = mock(IPage.class);
		final IPage defaultListPage = mock(IPage.class);

		checking(new Expectations()
		{
			{
				exactly(1).of(cycle).getPage("FooSearch"); will(returnValue(fooSearchPage));
				exactly(1).of(cycle).getPage("FooEdit"); will(throwException(new PageNotFoundException("blah")));
				exactly(1).of(cycle).getPage("DefaultEdit"); will(returnValue(defaultEditPage));
				exactly(1).of(cycle).getPage("FooList"); will(throwException(new PageNotFoundException("blah")));
				exactly(1).of(cycle).getPage("DefaultList"); will(returnValue(defaultListPage));
			}
		});

		assertEquals(fooSearchPage, pageResolver.resolvePage(cycle, Foo.class, PageType.SEARCH));
		assertEquals(defaultEditPage, pageResolver.resolvePage(cycle, Foo.class, PageType.EDIT));
		assertEquals(defaultListPage, pageResolver.resolvePage(cycle, Foo.class, PageType.LIST));

	}
}
