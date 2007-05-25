package org.trails.component;

import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceService;

/**
 * @author Chris Nelson
 *         <p/>
 *         This guy interacts with persistence service to produce a Select
 *         containing all the elements of the PropertyDescriptor's type.  If
 *         a criteria is specified, it will filter the list by it.
 */
@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class AssociationSelect extends BaseComponent
{
	@InjectObject("spring:persistenceService")
	public abstract PersistenceService getPersistenceService();

	@InjectObject("spring:descriptorService")
	public abstract DescriptorService getDescriptorService();

	public abstract IPropertySelectionModel getPropertySelectionModel();

	public abstract void setPropertySelectionModel(IPropertySelectionModel PropertySelectionModel);

	@Parameter(required = true, cache = true)
	public abstract Object getModel();

	public abstract void setModel(Object bytes);

	@Parameter
	public abstract Object getValue();

	public abstract void setValue(Object value);


	@Parameter(required = true, cache = true)
	public abstract IPropertyDescriptor getPropertyDescriptor();

	public abstract void setPropertyDescriptor(IPropertyDescriptor PropertyDescriptor);

	@Parameter(required = false, defaultValue = "not(propertyDescriptor.required)")
	public abstract boolean isAllowNone();

	public abstract void setAllowNone(boolean AllowNone);

	public abstract String getNoneLabel();

	public abstract void setNoneLabel(String NoneLabel);

	@Parameter(required = false)
	public abstract List getInstances();

	public abstract void setInstances(List instances);

	public AssociationSelect()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public IClassDescriptor getClassDescriptor()
	{
		return getDescriptorService().getClassDescriptor(getPropertyDescriptor().getPropertyType());
	}

	@Override
	protected void prepareForRender(IRequestCycle arg0)
	{
		// Leaving this in causes the select to always use the same
		// property selectio model.  Not sure why yet :(
//        if (getPropertySelectionModel() == null)
//        {
		buildSelectionModel();
//        }
		super.prepareForRender(arg0);
	}

	public void buildSelectionModel()
	{
		IdentifierSelectionModel selectionModel;
		if (getInstances() != null)
		{
			selectionModel = new IdentifierSelectionModel(getInstances(),
				getClassDescriptor().getIdentifierDescriptor().getName(),
				isAllowNone());
		} else
		{
			selectionModel = new IdentifierSelectionModel(getPersistenceService().getAllInstances(getClassDescriptor().getType()),
				getClassDescriptor().getIdentifierDescriptor().getName(),
				isAllowNone());
		}
		selectionModel.setNoneLabel(getNoneLabel());
		setPropertySelectionModel(selectionModel);
	}


}
