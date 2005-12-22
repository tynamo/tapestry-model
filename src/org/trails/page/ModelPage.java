package org.trails.page;

import ognl.Ognl;
import ognl.OgnlException;

import org.trails.TrailsRuntimeException;
import org.trails.descriptor.IClassDescriptor;
import org.trails.hibernate.HasAssignedIdentifier;

/**
 * A page which has a model object.
 * 
 * @author Chris Nelson
 *
 */
public abstract class ModelPage extends TrailsPage
{

    public ModelPage()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public abstract Object getModel();

    public abstract void setModel(Object model);

    /**
     * @return
     */
    public boolean isModelNew()
    {
        if (getModel() instanceof HasAssignedIdentifier)
        {
            return !((HasAssignedIdentifier)getModel()).isSaved();
        }
        else
        {
            try
            {
                return Ognl.getValue("model[classDescriptor.identifierDescriptor.name]", this) == null;
            } catch (OgnlException e)
            {
                throw new TrailsRuntimeException(e);
            }
        }
    }

    /**
     * @return
     */
    public IClassDescriptor getClassDescriptor()
    {
        return getDescriptorService().getClassDescriptor(getModel().getClass());
    }

}
