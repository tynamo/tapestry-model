package org.tynamo.component;

import org.tynamo.descriptor.extension.EnumReferenceDescriptor;
import org.tynamo.descriptor.IPropertyDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;
import org.tynamo.test.Gazonk;

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
		EnumPropertySelectionModel selectionModel = (EnumPropertySelectionModel) enumSelect.buildSelectionModel();
		assertEquals(6, selectionModel.getOptionCount());

		enumSelect.setAllowNone(false);
		selectionModel = (EnumPropertySelectionModel) enumSelect.buildSelectionModel();
		assertEquals(5, selectionModel.getOptionCount());

		enumSelect.setNoneLabel("Any");

		selectionModel = (EnumPropertySelectionModel) enumSelect.buildSelectionModel();
		assertEquals("Any", selectionModel.getNoneLabel());
	}
}
