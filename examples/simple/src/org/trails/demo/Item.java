package org.trails.demo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.commons.lang.builder.EqualsBuilder;

@Entity
public class Item
{
    private Integer id;
    
    private String name;
    
    private Set<Item2> item2s = new HashSet<Item2>();
    
    public Item()
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

    @ManyToMany
    public Set<Item2> getItem2s()
    {
        return item2s;
    }

    public void setItem2s(Set<Item2> item2s)
    {
        this.item2s = item2s;
    }

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
        if (this == obj)
            return true;
        try
        {
            final Item many = (Item) obj;
            if (!getId().equals(many.getId()))
                return false;
            return true;
        } catch (Exception e)
        {
            return false;
        }

    }

    @Override
    public String toString()
    {
        return getName();
    }

    
}
