/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.demo;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.validation.ValidateUniqueness;


/**
 * @hibernate.class
 * @javabean.class shortDescription="This is some other descriptive text."
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
public class Catalog
{
    private Integer id;
    private String name;
    private List<Category> categories = new ArrayList<Category>();

    /**
     * @hibernate.id generator-class="native"
     * @javabean.property
     */
    @Id(generate=GeneratorType.AUTO)
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @hibernate.property
     * @javabean.property
     */
    @NotNull
    @Length(min=1,max=20)
    @Pattern(regex="[a-z]*")
    @PropertyDescriptor(index=2)
    public String getName()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return getName();
    }

    /**
     * @hibernate.list cascade="all-delete-orphan"
     * @hibernate.collection-key column="CATALOG_ID"
     * @hibernate.collection-index column = "CATEGORY_INDEX"
     * @hibernate.collection-one-to-many class="org.trails.demo.Category"
     * @javabean.property
     *
     */
    @OneToMany(cascade=javax.persistence.CascadeType.ALL)
    @JoinColumn(name="CATALOG_ID")
    @IndexColumn(name="CATEGORY_INDEX")
    @PropertyDescriptor(index=1)
    @Collection(child=true)
    public List<Category> getCategories()
    {
        return categories;
    }

    /**
     * @param categories The categories to set.
     */
    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
        
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        try
        {
            final Catalog catalog = (Catalog) obj;
            if (!getId().equals(catalog.getId()))
                return false;
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
}
