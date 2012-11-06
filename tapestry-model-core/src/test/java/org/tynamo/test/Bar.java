package org.tynamo.test;


public class Bar implements IBar
{

	private Integer id;

	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId()
	{
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getTransientProperty()
	{
		return "Hello World";
	}
}
