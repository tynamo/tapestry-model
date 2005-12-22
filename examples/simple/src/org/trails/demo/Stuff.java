package org.trails.demo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.trails.descriptor.annotation.Collection;

@Entity
public class Stuff
{

    public Stuff()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    private Integer id;
    
    private String name;

    private Set<Thing> things = new HashSet<Thing>();
    
    @Id(generate=GeneratorType.AUTO)
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @OneToMany(cascade=CascadeType.ALL)
    
    public Set<Thing> getThings()
    {
        return things;
    }

    public void setThings(Set<Thing> things)
    {
        this.things = things;
    }
}

