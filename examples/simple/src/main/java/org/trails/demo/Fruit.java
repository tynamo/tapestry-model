package org.trails.demo;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.trails.descriptor.annotation.DescriptorAnnotation;
import org.trails.descriptor.annotation.PropertyDescriptor;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Fruit
{
    private Integer id;
    
    public Fruit()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @PropertyDescriptor(index=0)
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
    
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
