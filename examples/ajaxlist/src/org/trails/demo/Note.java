package org.trails.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

@Entity
public class Note
{
    private Integer id;

    @Id(generate = GeneratorType.AUTO)
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public Note()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private String note;

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

}
