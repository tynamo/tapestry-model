package org.trailsframework.hibernate.validation;

import org.trailsframework.descriptor.TrailsPropertyDescriptor;
import org.trailsframework.exception.ValidationException;

public class UniquenessException extends ValidationException
{
	public static final String DEFAULT_MESSAGE = "{0} must be unique.";

	public UniquenessException()
	{
		super();
	}

	public UniquenessException(TrailsPropertyDescriptor descriptor, String message)
	{
		super(descriptor, message);
	}

	public UniquenessException(TrailsPropertyDescriptor descriptor)
	{
		super(descriptor, DEFAULT_MESSAGE);
	}

	public UniquenessException(String message)
	{
		super(message);
	}

}
