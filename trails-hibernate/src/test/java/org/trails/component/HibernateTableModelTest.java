package org.trails.component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.services.ExpressionEvaluator;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.impl.CriteriaImpl;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.testhibernate.Foo;

public class HibernateTableModelTest extends MockObjectTestCase
{

	HibernateTableModel trailsTableModel;
	DetachedCriteria criteria;
	ArrayList foos;
	Foo foo;
	Mock persistenceServiceMock;

	public void setUp() throws Exception
	{
		foos = new ArrayList();
		foo = new Foo();
		foos.add(foo);

		criteria = DetachedCriteria.forClass(Foo.class);
		persistenceServiceMock = new Mock(HibernatePersistenceService.class);
		trailsTableModel = new HibernateTableModel((HibernatePersistenceService) persistenceServiceMock.proxy(), criteria);
	}

	DetachedCriteria argCriteria;

	public void testGetCurrentPageRows() throws Exception
	{
		persistenceServiceMock.expects(once()).method("getInstances")
			.with(new Constraint()
			{

				public boolean eval(Object param)
				{
					argCriteria = (DetachedCriteria) param;
					return true;
				}

				public StringBuffer describeTo(StringBuffer buffer)
				{
					buffer.append("Arg matching criteria");
					return buffer;
				}

			}, eq(0), eq(10))
			.will(returnValue(foos));
		Mock exprEvalMock = new Mock(ExpressionEvaluator.class);
		TrailsPropertyDescriptor propDescriptor = new TrailsPropertyDescriptor(Foo.class, "name", String.class);
		TrailsTableColumn column = new TrailsTableColumn(propDescriptor, (ExpressionEvaluator) exprEvalMock.proxy());
		Iterator iter = trailsTableModel.getCurrentPageRows(0, 10, column, true);
		assertTrue(iter.hasNext());
		assertEquals(foo, iter.next());
		// some reflection evilness to make sure correct criteria were built
		Field criteriaImplField = DetachedCriteria.class.getDeclaredField("criteria");
		criteriaImplField.setAccessible(true);
		CriteriaImpl criteriaImpl = (CriteriaImpl) criteriaImplField.get(argCriteria);
		Field orderEntriesField = CriteriaImpl.class.getDeclaredField("orderEntries");
		orderEntriesField.setAccessible(true);
		List orderEntries = (List) orderEntriesField.get(criteriaImpl);
		assertEquals(1, orderEntries.size());
	}

	public void testRowCount() throws Exception
	{
		persistenceServiceMock.expects(once()).method("count")
			.with(isA(DetachedCriteria.class))
			.will(returnValue(5));
		assertEquals(5, trailsTableModel.getRowCount());
	}
}
