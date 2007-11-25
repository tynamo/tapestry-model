package org.trails.demo;

import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.*;

@Entity
public class Person
{

	private Integer id;

	private String firstName;

	private String lastName;

	private Car car;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@PropertyDescriptor(index = 0)
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	private Address address = new Address();

	@Embedded
	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	@OneToOne(mappedBy = "owner")
	public Car getCar()
	{
		return car;
	}

	public void setCar(Car car)
	{
		this.car = car;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Person person = (Person) o;

		return (id != null ? id.equals(person.id) : person.id == null);
	}

	public int hashCode()
	{
		return (id != null ? id.hashCode() : 0);
	}
}
