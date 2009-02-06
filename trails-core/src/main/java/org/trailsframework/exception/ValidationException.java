package org.trailsframework.exception;

import org.trailsframework.descriptor.TrailsPropertyDescriptor;

public class ValidationException extends PersistenceException
{

	private TrailsPropertyDescriptor descriptor;
	private String message;

	public ValidationException(TrailsPropertyDescriptor descriptor, String message)
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
