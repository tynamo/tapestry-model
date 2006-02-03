package org.trails.io;

import java.io.Serializable;

public class ObjectIdentity implements Serializable
{
	private final String entityName;
    private final Serializable id;

    public ObjectIdentity( String entityName, Serializable id )
    {
        this.entityName = entityName;
        this.id = id;
    }

    public String getEntityName()
    {
        return entityName;
    }

    public Serializable getId()
    {
        return id;
    }
}
