package org.trails.page;

import org.apache.tapestry.annotations.InjectObject;
import org.trails.callback.EditCallback;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.validation.ValidatorTranslatorService;

/**
 *
 * This page contains all the default editor Blocks.  This allows
 * Trails to dynamically pick at runtime which editor to use for a
 * specific property.
 * @author Chris Nelson
 *
 */
public abstract class EditorBlockPage extends ModelPage
{
    @Override
    public void pushCallback()
    {
        EditCallback callback = new EditCallback(getEditPageName(), getModel());
        getCallbackStack().push(callback);
    }

    public abstract IPropertyDescriptor getDescriptor();

    public abstract void setDescriptor(IPropertyDescriptor Descriptor);

    public abstract String getEditPageName();

    public abstract void setEditPageName(String EditPageName);

    @InjectObject("spring:validatorTranslatorService")
    public abstract ValidatorTranslatorService getValidatorTranslatorService();
}
