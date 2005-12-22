package org.trails.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.trails.descriptor.annotation.PropertyDescriptor;

@Entity
public class VersionedThing
{
    private Integer id;
    
    public VersionedThing()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    @Id(generate=GeneratorType.AUTO)
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    private Integer version;

    @Version
    @PropertyDescriptor(readOnly=true)
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }
}
