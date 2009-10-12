package org.tynamo.examples.recipe.model;

import org.tynamo.descriptor.annotation.ClassDescriptor;
import org.tynamo.descriptor.annotation.PropertyDescriptor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ClassDescriptor(hidden = true)
public class Ingredient
{

	private Long id;

	private String amount;

	private String name;

	public Ingredient()
	{
	}

	public Ingredient(String amount, String name)
	{
		this.amount = amount;
		this.name = name;
	}

	public String getAmount()
	{
		return amount;
	}

	public void setAmount(String amount)
	{
		this.amount = amount;
	}

	@PropertyDescriptor(index = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
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

	public String toString()
	{
		return getAmount() + " " + getName();
	}


	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Ingredient)) return false;

		Ingredient that = (Ingredient) o;

		return getId() != null ? getId().equals(that.getId()) : that.getId() == null;

	}

	@Override
	public int hashCode()
	{
		return getId() != null ? getId().hashCode() : 0;
	}
}
