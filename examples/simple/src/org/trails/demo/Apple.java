package org.trails.demo;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import org.apache.commons.lang.builder.EqualsBuilder;

@Entity
@Inheritance
public class Apple extends Fruit
{

    public Apple()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private String color;

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }
    
	@Override
	public boolean equals(Object obj)
	{
		// TODO Auto-generated method stub
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
