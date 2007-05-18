package org.trails.component.search;

import org.hibernate.criterion.DetachedCriteria;
import org.trails.component.ComponentTest;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

public class SimpleSearchFieldTest extends ComponentTest
{
	public void testAddCriterion() throws Exception
	{
		SimpleSearchField stringSearchField = (SimpleSearchField) creator.newInstance(SimpleSearchField.class);
		DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
		stringSearchField.setCriteria(criteria);
		IPropertyDescriptor propDescriptor = new TrailsPropertyDescriptor(Foo.class, "name", String.class);
		stringSearchField.setPropertyDescriptor(propDescriptor);
		stringSearchField.buildCriterion();
		assertTrue(stringSearchField.getCriteria().toString().indexOf("name=") == -1);

		stringSearchField.setValue("bunk");
		stringSearchField.buildCriterion();
		assertTrue(stringSearchField.getCriteria().toString().indexOf("name=bunk") > -1);

	}
}
