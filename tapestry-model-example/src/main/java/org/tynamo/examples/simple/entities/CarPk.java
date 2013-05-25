package org.tynamo.examples.simple.entities;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
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

/*
	private Make make;

	private CarModel carModel;

	@Transient
//	#todo @InitialValue("model.make")
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
//	#todo @PossibleValues("make.models")
	public CarModel getCarModel()
	{
		return carModel;
	}

	public void setCarModel(CarModel carModel)
	{
		this.carModel = carModel;
	}
*/

/*
	public String toString()
	{
		return getCarModel() == null ? null : getCarModel().toString() + ", " + getCarModel().getMake().toString() + ", " + name;
	}
*/

}
