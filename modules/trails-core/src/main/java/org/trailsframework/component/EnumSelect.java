package org.trails.component;

import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.descriptor.extension.EnumReferenceDescriptor;

/**
 * Produces a select list for native java enum types.
 */
@ComponentClass(allowBody = false, allowInformalParameters = true)
public abstract class EnumSelect extends AbstractPropertySelection
{

	@Override
	public IPropertySelectionModel buildSelectionModel()
	{
		EnumPropertySelectionModel selectionModel = new EnumPropertySelectionModel(getPropertyDescriptor().getExtension(EnumReferenceDescriptor.class).getPropertyType(), isAllowNone());
		selectionModel.setNoneLabel(getNoneLabel());
		return selectionModel;
	}
}
