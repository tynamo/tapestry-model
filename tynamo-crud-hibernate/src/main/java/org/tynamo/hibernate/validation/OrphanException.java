package org.tynamo.hibernate.validation;

import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.exception.ValidationException;

public class OrphanException extends ValidationException
{

	public OrphanException()
	{
		super();
	}

	public OrphanException(TynamoPropertyDescriptor descriptor, String message)
	{
		super(descriptor, message);
	}

	public OrphanException(String message)
	{
		super(message);
	}

}
