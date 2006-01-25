package org.trails.component.search;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.component.ComponentTest;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

public class SearchFormTest extends ComponentTest
{
	public void testBuildCriteria() throws Exception
	{
		SearchForm searchForm = (SearchForm)creator.newInstance(SearchForm.class);
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
		assertTrue(searchForm.buildCriteria().toString().indexOf("name=aname") > -1);
		assertTrue(searchForm.buildCriteria().toString().indexOf("number=3.2") > -1);
		
	}
}
