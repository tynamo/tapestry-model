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
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;


/**
 * @hibernate.class table="CATEGORY"
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
public class Category
{
    private Integer id;
    private String description;
    private String name;
    private List<Product> products = new ArrayList<Product>();
    
    /**
     * @hibernate.property not-null="true"
     */
    @NotNull
    @Length(min=1,max=20)
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @hibernate.id generator-class="native"
     */
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        // TODO Auto-generated method stub
        return getDescription();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Category)) 
        {
            return false;
        }
        Category cat =  ((Category)obj);
        if (cat == null) return false;
        if (cat.getId() != null)
        {
            return cat.getId().equals(getId());
        }
        else
        {
            return getId() == null;
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
    
    /**
     * @hibernate.property not-null="true"
     */
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
    
    /**
     * @hibernate.list cascade="save-update" table="category_member"
     * @hibernate.collection-key column="category"
     * @hibernate.collection-index column = "product_index"
     * @hibernate.collection-many-to-many class="org.trails.demo.Product"
     * @javabean.property
     *
     */
    @OneToMany
    @JoinColumn(name="CATEGORY_ID")
    @IndexColumn(name="PRODUCT_INDEX")
    public List<Product> getProducts()
    {
        return products;
    }
    
    /**
     * @param products The products to set.
     */
    public void setProducts(List<Product> products)
    {
        this.products = products;
    }
}
