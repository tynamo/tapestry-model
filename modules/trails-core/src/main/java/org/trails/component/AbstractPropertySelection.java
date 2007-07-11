package org.trails.component;

import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;
import org.trails.descriptor.IPropertyDescriptor;

public abstract class AbstractPropertySelection extends BaseComponent
{

	public abstract IPropertySelectionModel getPropertySelectionModel();

	public abstract void setPropertySelectionModel(IPropertySelectionModel PropertySelectionModel);

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

	@Parameter(required = false, defaultValue = "page.model")
	public abstract Object getModel();

	public abstract void setModel(Object bytes);


	@Component(type = "PropertySelection", inheritInformalParameters = true,
		bindings = {"value=model[propertyDescriptor.name]", "model=propertySelectionModel"})
	public abstract PropertySelection getPropertySelection();

	protected abstract IPropertySelectionModel buildSelectionModel();

	@Override
	protected void prepareForRender(IRequestCycle cycle)
	{
		super.prepareForRender(cycle);
		resetSelectionModel();
	}

	public void resetSelectionModel()
	{
		setPropertySelectionModel(buildSelectionModel());
	}
}
