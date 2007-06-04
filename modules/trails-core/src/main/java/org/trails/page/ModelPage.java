package org.trails.page;

import ognl.Ognl;
import ognl.OgnlException;
import org.trails.TrailsRuntimeException;
import org.trails.descriptor.IClassDescriptor;

/**
 * A page which has a model object.
 *
 * @author Chris Nelson
 */
public abstract class ModelPage extends TrailsPage
{

	public abstract Object getModel();

	public abstract void setModel(Object model);

	protected boolean isModelNew(Object model)
	{
		try
		{
			return Ognl.getValue(getClassDescriptor().getIdentifierDescriptor().getName(), model) == null;
		} catch (OgnlException e)
		{
			throw new TrailsRuntimeException(e);
		}
	}

	public boolean isModelNew()
	{
		return isModelNew(getModel());
	}

	public IClassDescriptor getClassDescriptor()
	{
		return getDescriptorService().getClassDescriptor(getModel().getClass());
	}

}
