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

import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;


@Entity
public class Catalog
{
	private Integer id;
	private String name;
	private List<Category> categories = new ArrayList<Category>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	@NotNull
	@Length(min = 1, max = 20)
	@Pattern(regex = "[a-z]*")
	@PropertyDescriptor(index = 2)
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

	@OneToMany(cascade = javax.persistence.CascadeType.ALL)
	@JoinColumn(name = "CATALOG_ID")
	@IndexColumn(name = "CATEGORY_INDEX")
	@PropertyDescriptor(index = 1)
	@Collection(child = true)
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
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Catalog catalog = (Catalog) o;

		return getId() != null ? getId().equals(catalog.getId()) : catalog.getId() == null;

	}

	@Override
	public int hashCode()
	{
		return (getId() != null ? getId().hashCode() : 0);
	}
}
