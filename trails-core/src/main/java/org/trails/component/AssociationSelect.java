package org.trails.component;

import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.persistence.PersistenceService;

/**
 * This guy interacts with persistence service to produce a Select
 * containing all the elements of the PropertyDescriptor's type.
 *
 * @author Chris Nelson
 */
@ComponentClass(allowBody = false, allowInformalParameters = true)
public abstract class AssociationSelect extends AbstractPropertySelection
{
	@InjectObject("spring:persistenceService")
	public abstract PersistenceService getPersistenceService();

	@InjectObject("spring:descriptorService")
	public abstract DescriptorService getDescriptorService();

	public IClassDescriptor getClassDescriptor()
	{
		return getDescriptorService().getClassDescriptor(getPropertyDescriptor().getPropertyType());
	}

	@Override
	protected IPropertySelectionModel buildSelectionModel()
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
		return selectionModel;
	}
}
