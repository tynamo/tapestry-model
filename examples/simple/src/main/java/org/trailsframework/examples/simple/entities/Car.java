package org.trailsframework.examples.simple.entities;

import org.hibernate.validator.Length;
import org.trailsframework.descriptor.annotation.PropertyDescriptor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class Car implements Serializable
{

	private CarPk id = new CarPk();

	private Person owner;

	private String notes;

	@EmbeddedId
	@PropertyDescriptor(index = 0)
	public CarPk getId()
	{
		return id;
	}

	public void setId(CarPk id)
	{
		this.id = id;
	}

	@PropertyDescriptor(index = 1)
	@Length(max = 350)
	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	@OneToOne
	public Person getOwner()
	{
		return owner;
	}

	public void setOwner(Person owner)
	{
		this.owner = owner;
	}
}
