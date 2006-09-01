package org.trails.component;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.EnumReferenceDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

public abstract class EnumSelect extends BaseComponent
{

    @InjectObject("spring:descriptorService")
    public abstract DescriptorService getDescriptorService();

    public abstract IPropertySelectionModel getPropertySelectionModel();

    public abstract void setPropertySelectionModel(IPropertySelectionModel PropertySelectionModel);

    public abstract IPropertyDescriptor getPropertyDescriptor();

    public abstract void setPropertyDescriptor(IPropertyDescriptor PropertyDescriptor);

    public abstract boolean isAllowNone();

    public abstract void setAllowNone(boolean AllowNone);

    public abstract String getNoneLabel();

    public abstract void setNoneLabel(String NoneLabel);

    public EnumSelect()
    {
        super();
    }

    @Override
    protected void prepareForRender(IRequestCycle arg0)
    {
        buildSelectionModel();
        super.prepareForRender(arg0);
    }

    public void buildSelectionModel()
    {
        EnumSelectionModel selectionModel = new EnumSelectionModel(getPropertyDescriptor().getExtension(EnumReferenceDescriptor.class).getPropertyType(), isAllowNone());
        selectionModel.setNoneLabel(getNoneLabel());
        setPropertySelectionModel(selectionModel);
    }


}
