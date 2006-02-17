package org.trails.demo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;

@Entity
public class Make
{

    public Make()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private Integer id;
    
    private Set<Car> cars = new HashSet<Car>();

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
    
    @OneToMany(mappedBy="make")
	public Set<Car> getCars()
	{
		return cars;
	}

	public void setCars(Set<Car> cars)
	{
		this.cars = cars;
	}

    
}
