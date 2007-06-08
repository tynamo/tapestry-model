package org.trails.io;

import java.io.Serializable;

public class EntityIdentity implements Serializable
{

	private static final long serialVersionUID = 1L;

	private final String entityName;

	private final Serializable id;

	private final Serializable version;

	public EntityIdentity(String entityName, Serializable id)
	{
		this.entityName = entityName;
		this.id = id;
		this.version = null;
	}

	public EntityIdentity(String entityName, Serializable id, Serializable version)
	{
		this.entityName = entityName;
		this.id = id;
		this.version = version;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public Serializable getId()
	{
		return id;
	}

	public Serializable getVersion()
	{
		return version;
	}
}
