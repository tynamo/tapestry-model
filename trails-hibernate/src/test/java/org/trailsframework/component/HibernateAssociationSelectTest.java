package org.trails.component;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.jmock.Mock;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.testhibernate.Foo;

public class HibernateAssociationSelectTest extends HibernateComponentTest
{

	TrailsClassDescriptor classDescriptor;
	IPropertyDescriptor associationDescriptor;
	HibernateAssociationSelect associationSelect;

	public void setUp() throws Exception
	{

		persistenceMock = new Mock(HibernatePersistenceService.class);  // @todo: remove when the components reuse issue goes away
		classDescriptor = new TrailsClassDescriptor(Foo.class);
		classDescriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Foo.class, "id", Integer.class));
		descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").with(eq(Foo.class)).will(returnValue(classDescriptor));

		associationDescriptor = new TrailsPropertyDescriptor(Foo.class, "foo", Foo.class);
		associationSelect = (HibernateAssociationSelect) creator.newInstance(HibernateAssociationSelect.class,
			new Object[]{
				"hibernatePersistenceService", persistenceMock.proxy(), // @todo: remove when the components reuse issue goes away
				"descriptorService", descriptorServiceMock.proxy()
			});
		associationSelect.setPropertyDescriptor(associationDescriptor);
		associationSelect.setAllowNone(true);

	}

	public void testBuildSelectionModel()
	{

		List instances = new ArrayList();
		persistenceMock.expects(atLeastOnce()).method("getInstances").with(NOT_NULL, isA(DetachedCriteria.class)).will(returnValue(instances));

		AbstractPropertySelectionModel selectionModel = (AbstractPropertySelectionModel) associationSelect.buildSelectionModel();
		assertEquals(1, selectionModel.getOptionCount());

		associationSelect.setAllowNone(false);
		selectionModel = (AbstractPropertySelectionModel) associationSelect.buildSelectionModel();
		assertEquals(0, selectionModel.getOptionCount());

		associationSelect.setNoneLabel("Any");
		selectionModel = (AbstractPropertySelectionModel) associationSelect.buildSelectionModel();
		assertEquals("Any", selectionModel.getNoneLabel());
	}

	public void testGetClassDescriptor()
	{
		assertEquals(classDescriptor, associationSelect.getClassDescriptor());
	}
}