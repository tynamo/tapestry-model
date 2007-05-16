package org.trails.demo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
		if (this == obj)
			return true;
		try
		{
			final Make make = (Make) obj;
			if (!getId().equals(make.getId()))
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
		// TODO Auto-generated method stub
		return getName();
	}

	@OneToMany(mappedBy = "make")
	public Set<Car> getCars()
	{
		return cars;
	}

	public void setCars(Set<Car> cars)
	{
		this.cars = cars;
	}


}
