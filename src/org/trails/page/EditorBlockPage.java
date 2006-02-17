package org.trails.page;

import java.util.ArrayList;

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.form.translator.NumberTranslator;
import org.apache.tapestry.form.translator.StringTranslator;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.BaseValidator;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.NumberValidator;
import org.apache.tapestry.valid.StringValidator;
import org.trails.TrailsRuntimeException;
import org.trails.callback.EditCallback;
import org.trails.component.IdentifierSelectionModel;
import org.trails.descriptor.IClassDescriptor;
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
