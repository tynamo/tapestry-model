package org.trails.validation;

import java.text.MessageFormat;

import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceException;

public class ValidationException extends PersistenceException
{

    public static final String DEFAULT_MESSAGE = "{0} must be unique.";
    private String message;
    
    public ValidationException(IPropertyDescriptor descriptor)
    {
        this(descriptor, DEFAULT_MESSAGE);
    }
    
    public ValidationException(IPropertyDescriptor descriptor, String message)
    {
        this(MessageFormat.format(message, descriptor.getDisplayName()));
    }
    
    public ValidationException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public ValidationException(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ValidationException(String message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public ValidationException(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
