package org.trails.component.search;

import org.trails.component.ComponentTest;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

public class NumberSearchFieldTest extends ComponentTest
{
	/*
	 * Created test method otherwise the build failes...
	 */
	public void testGetTypeSpecificValue()
	{
		NumberSearchField numberSearchField = (NumberSearchField) creator.newInstance(
			NumberSearchField.class);
		TrailsPropertyDescriptor propDescriptor = new TrailsPropertyDescriptor(Foo.class, "id", Integer.class);
		numberSearchField.setPropertyDescriptor(propDescriptor);
		numberSearchField.setValue(new Long(2));
		assertEquals(new Integer(2), numberSearchField.getTypeSpecificValue());
	}
}
