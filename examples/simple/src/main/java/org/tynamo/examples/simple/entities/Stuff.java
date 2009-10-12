package org.tynamo.examples.simple.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Stuff
{

	private Integer id;

	private String name;

	private Set<Thing> things = new HashSet<Thing>();

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

	@OneToMany(cascade = CascadeType.ALL)
	public Set<Thing> getThings()
	{
		return things;
	}

	public void setThings(Set<Thing> things)
	{
		this.things = things;
	}
}

