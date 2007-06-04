package org.trails.callback;

import org.apache.tapestry.IRequestCycle;
import org.jmock.Mock;
import org.trails.component.ComponentTest;
import org.trails.page.SearchPage;
import org.trails.test.Foo;

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

		Mock cycleMock = new Mock(IRequestCycle.class);
		cycleMock.expects(once()).method("getPage").with(eq(pageName)).will(returnValue(searchPage));
		cycleMock.expects(once()).method("activate").with(same(searchPage));

		SearchCallback callBack = new SearchCallback(pageName, Foo.class.getName());
		callBack.performCallback((IRequestCycle) cycleMock.proxy());
		assertEquals(Foo.class.getName(), searchPage.getTypeName());
		cycleMock.verify();
	}

}
