package org.tynamo.exception;

import org.tynamo.descriptor.TynamoPropertyDescriptor;

public class ValidationException extends PersistenceException
{

	private TynamoPropertyDescriptor descriptor;
	private String message;

	public ValidationException(TynamoPropertyDescriptor descriptor, String message)
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
