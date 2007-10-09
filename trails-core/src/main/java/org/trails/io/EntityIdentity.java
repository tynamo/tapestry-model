package org.trails.io;

import java.io.Serializable;

public class EntityIdentity implements Serializable
{

	private static final long serialVersionUID = 1L;

	private final Class entityName;

	private final Serializable id;

	private final Object version;

	public EntityIdentity(Class entityName, Serializable id)
	{
		this(entityName, id, null);
	}

	public EntityIdentity(Class entityName, Serializable id, Object version)
	{
		this.entityName = entityName;
		this.id = id;
		this.version = version;
	}

	public Class getEntityName()
	{
		return entityName;
	}

	public Serializable getId()
	{
		return id;
	}

	public Object getVersion()
	{
		return version;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EntityIdentity that = (EntityIdentity) o;

		if (!entityName.equals(that.entityName)) return false;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;

		return true;
	}

	public int hashCode()
	{
		int result;
		result = entityName.hashCode();
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}
}
