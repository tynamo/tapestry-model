package org.trails.component.search;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.hibernate.criterion.DetachedCriteria;
import org.jmock.cglib.Mock;
import org.trails.component.ComponentTest;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.page.ListPage;
import org.trails.page.PageResolver;
import org.trails.page.TrailsPage.PageType;
import org.trails.test.Foo;

public class SearchFormTest extends ComponentTest
{
	SearchForm searchForm;
	Mock pageResolverMock;
	Mock cycleMock;
	
	public void setUp() throws Exception
	{
		pageResolverMock = new Mock(PageResolver.class);
		searchForm = (SearchForm)creator.newInstance(SearchForm.class, 
				new Object[] {"pageResolver", pageResolverMock.proxy()});
		IClassDescriptor fooDescriptor = new TrailsClassDescriptor(Foo.class, "Foo");
		searchForm.setClassDescriptor(fooDescriptor);		
		IPropertyDescriptor namePropDescriptor = new TrailsPropertyDescriptor(Foo.class, "name", String.class);
		SimpleSearchField nameSearchField = (SimpleSearchField)creator.newInstance(SimpleSearchField.class);
		nameSearchField.setPropertyDescriptor(namePropDescriptor);
		nameSearchField.setValue("aname");
		nameSearchField.setId("nameSearchField");
		IPropertyDescriptor numberPropDescriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
		SimpleSearchField numberSearchField = (SimpleSearchField)creator.newInstance(SimpleSearchField.class);
		numberSearchField.setValue(new Double(3.2));
		numberSearchField.setId("numberSearchField");
		numberSearchField.setPropertyDescriptor(numberPropDescriptor);
		searchForm.addComponent(nameSearchField);
		searchForm.addComponent(numberSearchField);
		cycleMock = new Mock(IRequestCycle.class);
	}
	
//	public void testBuildCriteria() throws Exception
//	{
//
//		assertTrue(searchForm.buildCriteria().toString().indexOf("name=aname") > -1);
//		assertTrue(searchForm.buildCriteria().toString().indexOf("number=3.2") > -1);
//		
//	}
	
	public void testSearch() throws Exception
	{
		ListPage listPage = (ListPage)creator.newInstance(ListPage.class);
		searchForm.setCriteria(DetachedCriteria.forClass(Foo.class));
		pageResolverMock.expects(once()).method("resolvePage").with(
				isA(IRequestCycle.class), 
				eq(Foo.class.getName()),
				eq(PageType.LIST)).will(returnValue(listPage));
		cycleMock.expects(once()).method("activate").with(eq(listPage));
		searchForm.search((IRequestCycle)cycleMock.proxy());
		assertNotNull(listPage.getCriteria());
	}
	
	public void testPageBeginRender() throws Exception
	{
		Mock pageMock = new Mock(IPage.class);
		PageEvent pageEvent = new PageEvent((IPage)pageMock.proxy(), (IRequestCycle)cycleMock.proxy());
		searchForm.pageBeginRender(pageEvent);
		assertNotNull(searchForm.getCriteria());
	}
}
