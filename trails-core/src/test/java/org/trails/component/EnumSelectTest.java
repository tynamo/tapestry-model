package org.trails.component;

import org.trails.descriptor.EnumReferenceDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Gazonk;

public class EnumSelectTest extends ComponentTest
{

	public EnumSelectTest()
	{
		super();
	}

	TrailsClassDescriptor classDescriptor;
	IPropertyDescriptor associationDescriptor;
	EnumSelect enumSelect;

	public void setUp() throws Exception
	{

		EnumReferenceDescriptor enumReferenceDescriptor = new EnumReferenceDescriptor(Gazonk.Origin.class);

		classDescriptor = new TrailsClassDescriptor(Gazonk.class);
		classDescriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Gazonk.class, "id", Integer.class));

		associationDescriptor = new TrailsPropertyDescriptor(Gazonk.Origin.class, "origin", Gazonk.Origin.class);
		associationDescriptor.addExtension(EnumReferenceDescriptor.class.getName(), enumReferenceDescriptor);


		enumSelect = (EnumSelect) creator.newInstance(EnumSelect.class);

		enumSelect.setPropertyDescriptor(associationDescriptor);
		enumSelect.setAllowNone(true);

	}

	public void testBuildSelectionModel()
	{
		enumSelect.resetSelectionModel();
		EnumPropertySelectionModel selectionModel = (EnumPropertySelectionModel) enumSelect.getPropertySelectionModel();
		assertEquals(6, selectionModel.getOptionCount());

		enumSelect.setAllowNone(false);
		enumSelect.resetSelectionModel();
		selectionModel = (EnumPropertySelectionModel) enumSelect.getPropertySelectionModel();
		assertEquals(5, selectionModel.getOptionCount());

		enumSelect.setNoneLabel("Any");
		enumSelect.resetSelectionModel();

		selectionModel = (EnumPropertySelectionModel) enumSelect.getPropertySelectionModel();
		assertEquals("Any", selectionModel.getNoneLabel());

	}
}
