package org.trails.component.search;

import org.trails.component.ComponentTest;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

public class SimpleSearchFieldTest extends ComponentTest
{
	public void testAddCriterion() throws Exception
	{
		SimpleSearchField stringSearchField = (SimpleSearchField)creator.newInstance(SimpleSearchField.class);
		IPropertyDescriptor propDescriptor = new TrailsPropertyDescriptor(Foo.class, "name", String.class);
		stringSearchField.setValue("bunk");
		stringSearchField.setPropertyDescriptor(propDescriptor);
		assertEquals("name=bunk", stringSearchField.buildCriterion().toString());
	}
}
