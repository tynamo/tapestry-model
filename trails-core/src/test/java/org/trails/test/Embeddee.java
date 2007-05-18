package org.trails.test;

import org.trails.descriptor.annotation.PropertyDescriptor;

public class Embeddee
{

	private String title;

	private String description;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@PropertyDescriptor(displayName = "The Title")
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	//@note: Added to expose a bug
	public boolean isTrue()
	{
		return true;
	}
}
