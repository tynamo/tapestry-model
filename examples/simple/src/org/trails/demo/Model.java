package org.trails.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;

@Entity
public class Model
{

    public Model()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    private Integer id;

    @Id(generate = GeneratorType.AUTO)
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

    @Override
    public boolean equals(Object obj)
    {
        // TODO Auto-generated method stub
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return getName();
    }

}
