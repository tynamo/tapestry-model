package org.trails.test;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

@Entity
public class Embeddor
{
	private Integer id;
	
	private String name;

	@Id(generate=GeneratorType.AUTO)
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
	
	private Embeddee embeddee;

	@Embedded
	public Embeddee getEmbeddee()
	{
		return embeddee;
	}

	public void setEmbeddee(Embeddee embeddee)
	{
		this.embeddee = embeddee;
	}
}
