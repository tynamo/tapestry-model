package org.tynamo.component.search;

import org.hibernate.criterion.DetachedCriteria;
import org.tynamo.component.HibernateComponentTest;
import org.tynamo.descriptor.IPropertyDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;
import org.tynamo.testhibernate.Foo;

public class SimpleSearchFieldTest extends HibernateComponentTest
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
