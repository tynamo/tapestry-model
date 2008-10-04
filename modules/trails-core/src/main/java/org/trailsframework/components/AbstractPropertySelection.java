package org.trails.component;

import java.util.List;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.IOptionRenderer;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.trails.descriptor.IPropertyDescriptor;

/**
 * A base class for building trails-aware PropertySelection components
 * Notice that this class uses the PropertySelection render mechanism,
 * that means that derivative classes can't make use of HTML templates.
  */
public abstract class AbstractPropertySelection extends PropertySelection
{

	@Parameter(defaultValue = "buildSelectionModel()")
	public abstract IPropertySelectionModel getModel();

	public abstract void setModel(IPropertySelectionModel PropertySelectionModel);

	@InjectObject("service:tapestry.form.ValidatableFieldSupport")
	public abstract ValidatableFieldSupport getValidatableFieldSupport();

	@Parameter(cache = false, defaultValue = "ognl:@org.apache.tapestry.form.DefaultOptionRenderer@DEFAULT_INSTANCE")
	public abstract IOptionRenderer getOptionRenderer();

	@Parameter(name = "id", defaultValue = "id")
	public abstract String getIdParameter();

	@Parameter(required = true)
	public abstract IPropertyDescriptor getPropertyDescriptor();

	public abstract void setPropertyDescriptor(IPropertyDescriptor PropertyDescriptor);

	@Parameter(defaultValue = "not(propertyDescriptor.required)")
	public abstract boolean isAllowNone();

	public abstract void setAllowNone(boolean allowNone);

	@Parameter(defaultValue = "literal:" + AbstractPropertySelectionModel.DEFAULT_NONE_LABEL)
	public abstract String getNoneLabel();

	public abstract void setNoneLabel(String noneLabel);

	@Parameter
	public abstract List getInstances();

	public abstract void setInstances(List instances);

	@Parameter
	public abstract Object getValue();

	public abstract void setValue(Object value);

	public abstract IPropertySelectionModel buildSelectionModel();

}
