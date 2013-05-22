package org.tynamo.examples.simple.entities;

import javax.persistence.Entity;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Length;
import org.tynamo.descriptor.annotation.PropertyDescriptor;

@Indexed
@Entity
public class Apple extends Fruit
{

	private String color;
	private String history;

	@Field
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
