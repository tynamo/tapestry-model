package org.trails.page;

import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.component.ComponentTest;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.persistence.PersistenceService;
import org.trails.test.Foo;
import org.trails.test.Searchee;

public class SearchPageTest extends ComponentTest
{

	PersistenceService persistenceService;

	ApplicationContext appContext;
	SearchPage searchPage;

	public void setUp() throws Exception
	{
		appContext = new ClassPathXmlApplicationContext(
			"applicationContext-test.xml");
		persistenceService = (PersistenceService) appContext.getBean(
			"persistenceService");
		searchPage = buildSearchPage();
	}

	public SearchPageTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

//    public void testSearch()
//    {
//        Mock cycleMock = new Mock(IRequestCycle.class);
//        ListPage searcheeListPage = buildTrailsPage(ListPage.class);
//        cycleMock.expects(atLeastOnce()).method("getPage")
//            .with(eq("SearcheeList"))
//            .will(returnValue(searcheeListPage));
//        cycleMock.expects(atLeastOnce()).method("activate").with(same(searcheeListPage));
//        
//        Searchee searchee1 = new Searchee();
//        searchee1.setName("searchee1");
//        Searchee searchee2 = new Searchee();
//        searchee2.setName("searchee2");
//        persistenceService.save(searchee1);
//        persistenceService.save(searchee2);
//        
//        // try with an empty one
//        searchPage.setExampleModel(new Searchee());
//        searchPage.search((IRequestCycle)cycleMock.proxy());
//        assertEquals("got 2 back", 2, searcheeListPage.getInstances().size());
//        // make sure callback is on stack
//        assertTrue("SearchCallback is on stack", 
//                searchPage.getCallbackStack().getStack().peek() instanceof SearchCallback);
//        // now try searching for one
//        searchPage.setExampleModel(searchee1);
//        searchPage.search((IRequestCycle)cycleMock.proxy());
//        assertEquals("got 1 back", 1, searcheeListPage.getInstances().size());
//        searchee1 = (Searchee)searcheeListPage.getInstances().get(0);
//        assertEquals("got searchee 1", "searchee1", searchee1.getName());
//        
//    }

	public void testGetSearchableProperties()
	{
		TrailsClassDescriptor classDescriptor = new TrailsClassDescriptor(Searchee.class);
		classDescriptor.getPropertyDescriptors().add(
			new TrailsPropertyDescriptor(Foo.class, "name", String.class));
		classDescriptor.getPropertyDescriptors().add(
			new IdentifierDescriptor(Foo.class, "id", String.class));
		classDescriptor.getPropertyDescriptors().add(
			new CollectionDescriptor(Foo.class, "name", Set.class));
		descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").with(eq(Searchee.class)).will(returnValue(classDescriptor));
		searchPage.setTypeName(Searchee.class.getName());
		String[] searchableProperties = searchPage.getSearchableProperties();
		assertEquals("should only be 2 search properties", 2, searchableProperties.length);
		assertEquals("name", searchableProperties[0]);
	}

	protected SearchPage buildSearchPage()
	{
		SearchPage searchPage = (SearchPage) creator.newInstance(SearchPage.class,
			new Object[]{
				"persistenceService", persistenceService,
				"descriptorService", descriptorServiceMock.proxy(),
				"callbackStack", callbackStack,
			});
		return searchPage;
	}


}
