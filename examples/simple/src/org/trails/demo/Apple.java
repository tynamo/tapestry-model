package org.trails.demo;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

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
}
