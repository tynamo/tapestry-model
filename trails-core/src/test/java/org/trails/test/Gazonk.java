package org.trails.test;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Gazonk
{
	public enum Origin
	{
		AFRICA, AMERICA, ASIA, EUROPE, OCEANIA
	}

	private Origin origin = Origin.ASIA;

	public enum Animal
	{
		CAT
			{
				public String toString()
				{
					return "Cat";
				}
				public String getSound()
				{
					return "Meow";
				}
			},
		DOG
			{
				public String toString()
				{
					return "Dog";
				}
				public String getSound()
				{
					return "Ruff!";
				}
			};

		public abstract String getSound();
	}

	private Integer id;

	private String name;

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


	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Enumerated(value = EnumType.STRING)
	public Origin getOrigin()
	{
		return origin;
	}

	public void setOrigin(Origin origen)
	{
		this.origin = origen;
	}

}
