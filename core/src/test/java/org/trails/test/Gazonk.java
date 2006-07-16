package org.trails.test;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Entity;

@Entity
public class Gazonk
{
    public enum Origin
    {
        AFRICA,AMERICA,ASIA,EUROPE,OCEANIA
    }

    private Origin origin = Origin.ASIA;


    private Integer id;

    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Enumerated(value = EnumType.STRING)
    public Origin getOrigin()
    {
        return origin;
    }

    public void setOrigin(Origin origen)
    {
        this.origin = origen;
    }

}
