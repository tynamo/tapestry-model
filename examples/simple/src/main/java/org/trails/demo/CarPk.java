package org.trails.demo;

import org.hibernate.validator.NotNull;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

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

	@NotNull
	@ManyToOne(optional = false)
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
		return getModel().toString() + ", " + getMake().toString() + ", " + name;
	}

}
