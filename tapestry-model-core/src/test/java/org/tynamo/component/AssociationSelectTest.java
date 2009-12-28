package org.tynamo.component;

import java.util.ArrayList;
import java.util.List;

import org.tynamo.descriptor.IPropertyDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;
import org.tynamo.test.Foo;

public class AssociationSelectTest extends ComponentTest
{

	TrailsClassDescriptor classDescriptor;
	IPropertyDescriptor associationDescriptor;
	AssociationSelect associationSelect;

	public void setUp() throws Exception
	{

		classDescriptor = new TrailsClassDescriptor(Foo.class);
		classDescriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Foo.class, "id", Integer.class));
		descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").with(eq(Foo.class)).will(returnValue(classDescriptor));

		associationDescriptor = new TrailsPropertyDescriptor(Foo.class, "foo", Foo.class);
		associationSelect = (AssociationSelect) creator.newInstance(AssociationSelect.class,
			new Object[]{
				"persistenceService", persistenceMock.proxy(),
				"descriptorService", descriptorServiceMock.proxy()
			});
		associationSelect.setPropertyDescriptor(associationDescriptor);
		associationSelect.setAllowNone(true);

	}

	public void testBuildSelectionModel()
	{

		List instances = new ArrayList();

		persistenceMock.expects(atLeastOnce()).method("getInstances").with(eq(Foo.class)).will(returnValue(instances));

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
