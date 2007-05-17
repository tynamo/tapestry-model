package org.trails.test;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.trails.descriptor.annotation.PropertyDescriptor;

@Entity
public class Searchee
{

	private Integer id;

	private String name;

	private String nonSearchableProperty;

	private Set<Foo> foos = new HashSet<Foo>();

	public Searchee()
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@PropertyDescriptor(searchable = false)
	public String getNonSearchableProperty()
	{
		return nonSearchableProperty;
	}

	public void setNonSearchableProperty(String nonSearchableProperty)
	{
		this.nonSearchableProperty = nonSearchableProperty;
	}

}
