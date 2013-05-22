package org.tynamo.examples.simple.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Address
{

	private String street;

	private String city;

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String toString()
	{
		if ((street != null) && (city != null))
		{
			return street + ", " + city;
		} else if (street != null)
		{
			return street;
		} else if (city != null)
		{
			return city;
		} else
		{
			return "";
		}
	}
}
