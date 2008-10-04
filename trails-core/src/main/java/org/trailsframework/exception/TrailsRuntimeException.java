package org.trails.exception;

/**
 * Runtime exceptions for Trails
 *
 * @author Chris Nelson
 */
public class TrailsRuntimeException extends RuntimeException
{

	private Class entityType;

	public TrailsRuntimeException(Exception e, Class entityType)
	{
		super(e);
		this.entityType = entityType;
	}

	public TrailsRuntimeException(String message)
	{
		super(message);
	}

	public TrailsRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TrailsRuntimeException(Throwable cause)
	{
		super(cause);
	}

	public Class getEntityType() {
		return entityType;
	}
}
