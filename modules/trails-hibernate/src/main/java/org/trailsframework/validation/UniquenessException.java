package org.trailsframework.validation;

import org.trailsframework.descriptor.IPropertyDescriptor;
import org.trailsframework.exception.ValidationException;

public class UniquenessException extends ValidationException
{
	public UniquenessException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public UniquenessException(IPropertyDescriptor descriptor, String message)
	{
		super(descriptor, message);
		// TODO Auto-generated constructor stub
	}

	public UniquenessException(IPropertyDescriptor descriptor)
	{
		super(descriptor);
		// TODO Auto-generated constructor stub
	}

	public UniquenessException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UniquenessException(String message, Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UniquenessException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
