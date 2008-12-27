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
package org.trailsframework.examples.recipe.model;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.trailsframework.descriptor.annotation.Collection;
import org.trailsframework.descriptor.annotation.PropertyDescriptor;
import org.trailsframework.util.Identifiable;
import org.trailsframework.validation.ValidateUniqueness;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@ValidateUniqueness(property = "title")
public class Recipe implements Identifiable
{
	private Long id;

	private String title;

	private String description;

	private String instructions;

	private Date date;

	@PropertyDescriptor(index = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@PropertyDescriptor(index = 1)
	@NotNull(message = "{error.emptyMessage}")
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	@PropertyDescriptor(index = 2)
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@PropertyDescriptor(index = 3, format = "MM/dd/yyyy")//, displayName = "Created On")
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

	@PropertyDescriptor(index = 6)
	@Length(max = 500)
	public String getInstructions()
	{
		return instructions;
	}

	public void setInstructions(String instructions)
	{
		this.instructions = instructions;
	}

	private Set<Ingredient> ingredients = new HashSet<Ingredient>();

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

}
