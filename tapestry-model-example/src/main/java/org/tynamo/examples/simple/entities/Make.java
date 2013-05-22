package org.tynamo.examples.simple.entities;

import org.tynamo.descriptor.annotation.CollectionDescriptor;
import org.tynamo.descriptor.annotation.PropertyDescriptor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Make implements Serializable
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

	private Set<CarModel> carModels = new HashSet<CarModel>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "make")
	@PropertyDescriptor(searchable = false)
	@CollectionDescriptor(addExpression = "addCarModel", removeExpression = "removeCarModel")
	public Set<CarModel> getCarModels() {
		return carModels;
	}

	public void setCarModels(final Set<CarModel> theCarModels)
	{
		carModels = theCarModels;
	}

	public void addCarModel(CarModel carModel) {
		carModel.setMake(this);
		carModels.add(carModel);
	}

	public void removeCarModel(CarModel carModel) {
		carModels.remove(carModel);
		carModel.setMake(null);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Make item = (Make) o;
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

