package org.tynamo.component.search;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.hibernate.criterion.DetachedCriteria;
import org.jmock.Mock;
import org.tynamo.component.HibernateComponentTest;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.IPropertyDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;
import org.tynamo.page.HibernateListPage;
import org.tynamo.page.PageResolver;
import org.tynamo.page.PageType;
import org.tynamo.testhibernate.Foo;
import org.tynamo.builder.BuilderDirector;

public class SearchFormTest extends HibernateComponentTest
{
	SearchForm searchForm;
	Mock pageResolverMock;
	Mock cycleMock;

	public void setUp() throws Exception
	{
		pageResolverMock = new Mock(PageResolver.class);
		TrailsClassDescriptor fooDescriptor = new TrailsClassDescriptor(Foo.class, "Foo");

		searchForm = (SearchForm) creator.newInstance(SearchForm.class,
				new Object[]{"pageResolver", pageResolverMock.proxy(), "classDescriptor", fooDescriptor, "builderDirector", new BuilderDirector()});

		IPropertyDescriptor namePropDescriptor = new TrailsPropertyDescriptor(Foo.class, "name", String.class);
		SimpleSearchField nameSearchField = (SimpleSearchField) creator.newInstance(SimpleSearchField.class);
		nameSearchField.setPropertyDescriptor(namePropDescriptor);
		nameSearchField.setValue("aname");
		nameSearchField.setId("nameSearchField");
		IPropertyDescriptor numberPropDescriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
		SimpleSearchField numberSearchField = (SimpleSearchField) creator.newInstance(SimpleSearchField.class);
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
		HibernateListPage listPage = (HibernateListPage) creator.newInstance(HibernateListPage.class);
		searchForm.setCriteria(DetachedCriteria.forClass(Foo.class));
		pageResolverMock.expects(once()).method("resolvePage").with(
				isA(IRequestCycle.class),
				eq(Foo.class),
				eq(PageType.LIST)).will(returnValue(listPage));
		cycleMock.expects(once()).method("activate").with(eq(listPage));
		searchForm.search((IRequestCycle) cycleMock.proxy());
		assertNotNull(listPage.getCriteria());
	}

	public void testPageBeginRender() throws Exception
	{
		Mock pageMock = new Mock(IPage.class);
		PageEvent pageEvent = new PageEvent((IPage) pageMock.proxy(), (IRequestCycle) cycleMock.proxy());
		searchForm.pageBeginRender(pageEvent);
		assertNotNull(searchForm.getCriteria());
		assertNotNull(searchForm.getModel());
	}
}
