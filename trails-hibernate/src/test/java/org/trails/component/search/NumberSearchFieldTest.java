package org.trails.component.search;

import org.trails.component.HibernateComponentTest;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.testhibernate.Foo;

public class NumberSearchFieldTest extends HibernateComponentTest
{
	/*
	 * Created test method otherwise the build fails...
	 */
	public void testGetTypeSpecificValue_Integer()
	{
		NumberSearchField numberSearchField = (NumberSearchField) creator.newInstance(
			NumberSearchField.class);
		TrailsPropertyDescriptor propDescriptor = new TrailsPropertyDescriptor(Foo.class, "id", Integer.class);
		numberSearchField.setPropertyDescriptor(propDescriptor);
		numberSearchField.setValue(new Long(2));
		assertEquals(new Integer(2), numberSearchField.getTypeSpecificValue());
	}

	public void testGetTypeSpecificValue_int() {
		NumberSearchField numberSearchField = (NumberSearchField) creator.newInstance(
			NumberSearchField.class);
		TrailsPropertyDescriptor propDescriptor = new TrailsPropertyDescriptor(Foo.class, "id", Integer.TYPE);
		numberSearchField.setPropertyDescriptor(propDescriptor);
		numberSearchField.setValue(new Long(2));
		assertEquals(new Integer(2), numberSearchField.getTypeSpecificValue());
	}
}

