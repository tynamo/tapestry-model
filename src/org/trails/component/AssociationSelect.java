package org.trails.component;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceService;

/**
 * 
 * @author Chris Nelson
 * 
 * This guy interacts with persistence service to produce a Select
 * containing all the elements of the PropertyDescriptor's type.  If
 * a criteria is specified, it will filter the list by it.
 *
 */
public abstract class AssociationSelect extends BaseComponent
{
    @InjectObject("spring:persistenceService")
    public abstract PersistenceService getPersistenceService();
    
    public abstract IPropertySelectionModel getPropertySelectionModel();

    public abstract void setPropertySelectionModel(IPropertySelectionModel PropertySelectionModel);
    
    public abstract DetachedCriteria getCriteria();

    public abstract void setCriteria(DetachedCriteria Criteria);
    
    public abstract IClassDescriptor getClassDescriptor();

    public abstract void setClassDescriptor(IClassDescriptor ClassDescriptor);
    
    public abstract IPropertyDescriptor getPropertyDescriptor();

    public abstract void setPropertyDescriptor(IPropertyDescriptor PropertyDescriptor);
 
    public AssociationSelect()
    {
        super();
        // TODO Auto-generated constructor stub
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
        DetachedCriteria criteria = getCriteria() != null ? 
                getCriteria() : 
                DetachedCriteria.forClass(getClassDescriptor().getType());
        IdentifierSelectionModel selectionModel = new IdentifierSelectionModel(
                getPersistenceService().getInstances(criteria),
                getClassDescriptor().getIdentifierDescriptor().getName(),
                !getPropertyDescriptor().isRequired());
        setPropertySelectionModel(selectionModel);
    }
    
    

}
