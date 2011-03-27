package org.tynamo.exception;

public class MetadataNotFoundException extends TynamoRuntimeException
{

	public MetadataNotFoundException(String message)
	{
		super(message);
	}

	public MetadataNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MetadataNotFoundException(Throwable cause)
	{
		super(cause);
	}

}
