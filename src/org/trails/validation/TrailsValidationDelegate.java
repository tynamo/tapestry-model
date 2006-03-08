package org.trails.validation;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.valid.FieldTracking;
import org.apache.tapestry.valid.IFieldTracking;
import org.apache.tapestry.valid.RenderString;
import org.apache.tapestry.valid.ValidationDelegate;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceException;

public class TrailsValidationDelegate extends ValidationDelegate
{
    
    public void record(PersistenceException ex)
    {
        FieldTracking tracking = findCurrentTracking();
        tracking.setErrorRenderer(new RenderString(ex.getMessage()));
        
    }

    public IFieldTracking getFieldTracking(String displayName)
    {
        if (_trackingsByDisplayName.containsKey(displayName))
        {
            return _trackingsByDisplayName.get(displayName);
        }
        else
        {
            return findCurrentTracking();
        }
    }

    protected Map<String,IFieldTracking> _trackingsByDisplayName = new HashMap<String,IFieldTracking>();
    
    @Override
    public void recordFieldInputValue(String input)
    {
        FieldTracking tracking = findCurrentTracking();

        tracking.setInput(input);
        _trackingsByDisplayName.put(tracking.getComponent().getDisplayName(), tracking);
        
    }

    public void record(IClassDescriptor descriptor, InvalidStateException invalidStateException)
    {
        for (InvalidValue invalidValue : invalidStateException.getInvalidValues())
        {

            IPropertyDescriptor propertyDescriptor = descriptor.getPropertyDescriptor(invalidValue.getPropertyName());
            FieldTracking fieldTracking = (FieldTracking)
                getFieldTracking(propertyDescriptor.getDisplayName());
            String message = propertyDescriptor.getDisplayName() + " " + invalidValue.getMessage(); 
                   
            fieldTracking.setErrorRenderer(new RenderString(message));
           
        }
    }
    
}
