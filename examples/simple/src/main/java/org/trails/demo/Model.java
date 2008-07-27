package org.trails.demo;

import org.apache.commons.lang.builder.EqualsBuilder;

import javax.persistence.*;

import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.PropertyDescriptor;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Model implements Serializable
{

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

	private Make make;

	@ManyToOne
	@NotNull
	public Make getMake()
	{
		return make;
	}

	public void setMake(Make make)
	{
		this.make = make;
	}

	@OneToMany(mappedBy = "id.model")
	@PropertyDescriptor(readOnly = true)
	public Set<Car> getCars()
	{
		return cars;
	}

	public void setCars(Set<Car> cars)
	{
		this.cars = cars;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Model item = (Model) o;
		return getId() != null ? getId().equals(item.getId()) : item.getId() == null;
	}

	@Override
	public int hashCode()
	{
		return (getId() != null ? getId().hashCode() : 0);
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
