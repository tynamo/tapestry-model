package org.trailsframework.hibernate.validation;

import org.trailsframework.descriptor.IPropertyDescriptor;
import org.trailsframework.exception.ValidationException;

public class OrphanException extends ValidationException
{

	public OrphanException()
	{
		super();
	}

	public OrphanException(IPropertyDescriptor descriptor, String message)
	{
		super(descriptor, message);
	}

	public OrphanException(String message)
	{
		super(message);
	}

}
