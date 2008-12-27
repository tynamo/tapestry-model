package org.trailsframework.exception;

import org.trailsframework.descriptor.IPropertyDescriptor;

public class ValidationException extends PersistenceException
{

	private IPropertyDescriptor descriptor;
	private String message;

	public ValidationException(IPropertyDescriptor descriptor, String message)
	{
		this.descriptor = descriptor;
		this.message = message;
	}

	public ValidationException()
	{
		super();
	}

	public ValidationException(String message)
	{
		super(message);
	}

}
