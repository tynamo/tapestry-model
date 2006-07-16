package org.trails.validation;

import org.trails.TrailsRuntimeException;
import org.trails.descriptor.IPropertyDescriptor;

public class OrphanException extends ValidationException
{

	public OrphanException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public OrphanException(IPropertyDescriptor descriptor, String message)
	{
		super(descriptor, message);
		// TODO Auto-generated constructor stub
	}

	public OrphanException(IPropertyDescriptor descriptor)
	{
		super(descriptor);
		// TODO Auto-generated constructor stub
	}

	public OrphanException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public OrphanException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public OrphanException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
