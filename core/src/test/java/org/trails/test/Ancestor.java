package org.trails.test;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@Inheritance(
        strategy=InheritanceType.SINGLE_TABLE
       
    )
@DiscriminatorColumn(name="type")
public class Ancestor
{

    public Ancestor()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private Integer id;
    
    private String name;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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

    private Set<Baz> bazzes = new HashSet<Baz>();

    @OneToMany
    @JoinColumn(name="ancestor_id")
    public Set<Baz> getBazzes()
    {
        return bazzes;
    }

    public void setBazzes(Set<Baz> bazzes)
    {
        this.bazzes = bazzes;
    }
}
