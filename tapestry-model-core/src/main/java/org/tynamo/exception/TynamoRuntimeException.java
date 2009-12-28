package org.tynamo.exception;

/**
 * Runtime exceptions for Tynamo
 *
 * @author Chris Nelson
 */
public class TynamoRuntimeException extends RuntimeException
{

	private Class entityType;

	public TynamoRuntimeException(Exception e, Class entityType)
	{
		super(e);
		this.entityType = entityType;
	}

	public TynamoRuntimeException(String message)
	{
		super(message);
	}

	public TynamoRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TynamoRuntimeException(Throwable cause)
	{
		super(cause);
	}

	public Class getEntityType() {
		return entityType;
	}
}
