package org.tynamo.test;

import java.util.HashSet;
import java.util.Set;

import org.tynamo.descriptor.annotation.PropertyDescriptor;

public class Searchee
{

	private Integer id;

	private String name;

	private String nonSearchableProperty;

	private Set<Foo> foos = new HashSet<Foo>();

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	@PropertyDescriptor(searchable = true)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNonSearchableProperty()
	{
		return nonSearchableProperty;
	}

	public void setNonSearchableProperty(String nonSearchableProperty)
	{
		this.nonSearchableProperty = nonSearchableProperty;
	}

}
