package org.tynamo.test;

public class Embeddor
{

	private Integer id;

	private String name;

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

	public Embeddee getEmbeddee()
	{
		return embeddee;
	}

	public void setEmbeddee(Embeddee embeddee)
	{
		this.embeddee = embeddee;
	}
}
