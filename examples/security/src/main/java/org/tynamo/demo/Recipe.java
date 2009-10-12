/*
 * Created on Feb 6, 2005
 *
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
package org.tynamo.demo;

import org.hibernate.validator.NotNull;
import org.tynamo.descriptor.annotation.Collection;
import org.tynamo.descriptor.annotation.PropertyDescriptor;
import org.tynamo.security.RestrictionType;
import org.tynamo.security.annotation.UpdateRequiresRole;
import org.tynamo.security.annotation.ViewRequiresRole;
import org.tynamo.validation.ValidateUniqueness;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@ValidateUniqueness(property = "title")
@UpdateRequiresRole("ROLE_MANAGER")
public class Recipe
{
    private Integer id;

    private String title;

    private String description;

    private Date date;

    @PropertyDescriptor(index = 0)
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

    @PropertyDescriptor(index = 1)
    @NotNull
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @PropertyDescriptor(index = 2)
		@ViewRequiresRole("ROLE_MANAGER")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @PropertyDescriptor(index = 3, format = "MM/dd/yyyy", displayName = "First Cooked On")
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    private Category category;

    @ManyToOne
    @PropertyDescriptor(index = 4)
    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    private Set<Ingredient> ingredients = new HashSet<Ingredient>();

    @PropertyDescriptor(index = 5)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipeId")
    @Collection(child = true)
    public Set<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients)
    {
        this.ingredients = ingredients;
    }

    private String instructions;

    @PropertyDescriptor(index = 6)
    public String getInstructions()
    {
        return instructions;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }

}
