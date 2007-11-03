package org.trails.demo;

import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category
{
	private Integer id;
	private String name;
	private List<SubCategory> subcategories = new ArrayList<SubCategory>();


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@PropertyDescriptor(hidden = true)
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	@NotNull
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}


	@OneToMany
	@Collection(child = true)
	public List<SubCategory> getSubcategories()
	{
		return subcategories;
	}

	public void setSubcategories(List<SubCategory> subcategories)
	{
		this.subcategories = subcategories;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Category category = (Category) o;

		return !(getId() != null ? !getId().equals(category.getId()) : category.getId() != null);
	}

	public int hashCode()
	{
		return (id != null ? id.hashCode() : 0);
	}

	public String toString()
	{
		return name;
	}
}