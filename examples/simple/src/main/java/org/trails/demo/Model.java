package org.trails.demo;

import org.apache.commons.lang.builder.EqualsBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.validator.NotNull;
import java.io.Serializable;

@Entity
public class Model implements Serializable
{

	private Integer id;

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
