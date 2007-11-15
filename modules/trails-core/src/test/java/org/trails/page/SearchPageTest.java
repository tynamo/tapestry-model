package org.trails.page;

import org.trails.component.ComponentTest;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;
import org.trails.test.Searchee;

import java.util.Set;

public class SearchPageTest extends ComponentTest
{

	SearchPage searchPage;

	public void setUp() throws Exception
	{
		searchPage = buildSearchPage();
	}

	public void testGetSearchableProperties()
	{
		TrailsClassDescriptor classDescriptor = new TrailsClassDescriptor(Searchee.class);
		classDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "name", String.class));
		classDescriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Foo.class, "id", String.class));
		classDescriptor.getPropertyDescriptors().add(new CollectionDescriptor(Foo.class, "name", Set.class));
		searchPage.setClassDescriptor(classDescriptor);

		// the classDescriptor is set on activateTrailsPage. Trails shouldn't ask the descriptorService for it 
		descriptorServiceMock.expects(never()).method("getClassDescriptor").with(eq(Searchee.class));

		String[] searchableProperties = searchPage.getSearchableProperties();
		assertEquals("should only be 2 search properties", 2, searchableProperties.length);
		assertEquals("name", searchableProperties[0]);
	}

	protected SearchPage buildSearchPage()
	{
		SearchPage searchPage = (SearchPage) creator.newInstance(SearchPage.class,
				new Object[]{
						"persistenceService", persistenceMock.proxy(),
						"descriptorService", descriptorServiceMock.proxy(),
						"callbackStack", callbackStack,
				});
		return searchPage;
	}


}
