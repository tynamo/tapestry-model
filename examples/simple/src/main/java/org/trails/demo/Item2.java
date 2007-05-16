package org.trails.demo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Item2
{

	private Integer id;

	private Set<Item> items = new HashSet<Item>();

	private String name;

	public Item2()
	{
		super();
		// TODO Auto-generated constructor stub
	}

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

	@ManyToMany
	public Set<Item> getItems()
	{
		return items;
	}

	public void setItems(Set<Item> items)
	{
		this.items = items;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		try
		{
			final Item2 many = (Item2) obj;
			if (!getId().equals(many.getId()))
				return false;
			return true;
		} catch (Exception e)
		{
			return false;
		}

	}

	@Override
	public String toString()
	{
		return getName();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
