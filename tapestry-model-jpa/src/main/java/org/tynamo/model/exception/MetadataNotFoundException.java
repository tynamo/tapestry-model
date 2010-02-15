package org.tynamo.model.exception;

import org.tynamo.exception.TynamoRuntimeException;

public class MetadataNotFoundException extends TynamoRuntimeException
{

	public MetadataNotFoundException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MetadataNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MetadataNotFoundException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
