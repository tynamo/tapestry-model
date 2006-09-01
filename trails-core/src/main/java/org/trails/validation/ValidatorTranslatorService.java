package org.trails.validation;

import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.form.translator.NumberTranslator;
import org.apache.tapestry.form.translator.StringTranslator;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.valid.BaseValidator;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.NumberValidator;
import org.apache.tapestry.valid.StringValidator;
import org.trails.descriptor.IPropertyDescriptor;

public class ValidatorTranslatorService
{
    /**
     * @param class1
     * @return
     */
    public IValidator getValidator(IPropertyDescriptor descriptor)
    {
        BaseValidator validator = null;

        if (descriptor.isNumeric())
        {
            validator = new NumberValidator();
            ((NumberValidator) validator).setValueTypeClass(descriptor.getPropertyType());
        }else
        {
            validator = new StringValidator();
        }
        validator.setRequired(descriptor.isRequired());
        return validator;
    }

    public Translator getTranslator(IPropertyDescriptor descriptor)
    {
        if (descriptor.isNumeric())
        {
            NumberTranslator numberTranslator = new NumberTranslator();
            if (descriptor.getFormat() != null) numberTranslator.setPattern(descriptor.getFormat());
            return numberTranslator;
        }
        else if(descriptor.isDate())
        {
            DateTranslator dateTranslator = new DateTranslator();
            if (descriptor.getFormat() != null) dateTranslator.setPattern(descriptor.getFormat());
            return dateTranslator;
        }
        else
        {
            return new StringTranslator();
        }
        
    }
}
