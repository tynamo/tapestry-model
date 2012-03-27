package org.tynamo.model.test.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Searchee {

    private Integer id;

    private String name;

    private String nonSearchableProperty;

    private Set<Foo> foos = new HashSet<Foo>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNonSearchableProperty() {
        return nonSearchableProperty;
    }

    public void setNonSearchableProperty(String nonSearchableProperty) {
        this.nonSearchableProperty = nonSearchableProperty;
    }

}
