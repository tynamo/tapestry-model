package org.trailsframework.examples.simple.entities;

import org.hibernate.validator.NotNull;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.io.Serializable;

import org.trailsframework.descriptor.annotation.PropertyDescriptor;
import org.trailsframework.descriptor.annotation.InitialValue;
import org.trailsframework.descriptor.annotation.PossibleValues;

@Embeddable
public class CarPk implements Serializable
{

	private String name;

	@NotNull
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	private Make make;

	private Model model;

	@Transient
	@PropertyDescriptor(summary = false)
	@InitialValue("model.make")
	public Make getMake()
	{
		return make;
	}

	public void setMake(Make make)
	{
		this.make = make;
	}

	@NotNull
	@ManyToOne(optional = false)
	@PossibleValues("make.models")
	public Model getModel()
	{
		return model;
	}

	public void setModel(Model model)
	{
		this.model = model;
	}

	public String toString()
	{
		return getModel().toString() + ", " + getModel().getMake().toString() + ", " + name;
	}

}
