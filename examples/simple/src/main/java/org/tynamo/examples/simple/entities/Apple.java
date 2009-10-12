package org.tynamo.examples.simple.entities;

import org.hibernate.validator.Length;
import org.tynamo.descriptor.annotation.PropertyDescriptor;

import javax.persistence.Entity;

@Entity
public class Apple extends Fruit
{

	private String color;
	private String history;

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	@PropertyDescriptor(richText = true)
	@Length(max = 3500)
	public String getHistory()
	{
		return history;
	}

	public void setHistory(String history)
	{
		this.history = history;
	}
}
