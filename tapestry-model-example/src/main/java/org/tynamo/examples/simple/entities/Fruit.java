package org.tynamo.examples.simple.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Fruit
{
	public enum Origin
	{
		AFRICA, AMERICA, ASIA, EUROPE, OCEANIA
	}

	private Integer id;

	private Origin origin = Origin.AMERICA;

	@DocumentId
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

	@Field
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@NotNull
	@Enumerated(value = EnumType.STRING)
	public Origin getOrigin()
	{
		return origin;
	}

	public void setOrigin(Origin origin)
	{
		this.origin = origin;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || !(o instanceof Fruit)) return false;

		Fruit fruit = (Fruit) o;

		return getId() != null ? getId().equals(fruit.getId()) : fruit.getId() == null;

	}

	public int hashCode()
	{
		return (getId() != null ? getId().hashCode() : 0);
	}
}
