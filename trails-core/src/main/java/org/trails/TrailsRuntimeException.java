package org.trails;

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
		super();
		this.entityType = entityType;
		// TODO Auto-generated constructor stub
	}

	public TrailsRuntimeException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TrailsRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TrailsRuntimeException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public Class getEntityType() {
		return entityType;
	}

}
