package org.trails.demo;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Category
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@PropertyDescriptor(hidden = true)
	private Integer id;

	private String name;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@IndexColumn(name = "CATEGORY_INDEX")
	@Collection(child = true, allowRemove = false, swapExpression = "swapSubCategory")
	private List<SubCategory> subcategories = new ArrayList<SubCategory>();


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


	public List<SubCategory> getSubcategories()
	{
		return Collections.unmodifiableList(subcategories);
	}

	public void addSubCategory(SubCategory subCategory)
	{
		subCategory.setCategory(this);
		if (!subcategories.contains(subCategory))
		{
			subcategories.add(subCategory);
			subCategory.setCategoryIndex(subcategories.size() - 1);
		}
	}

	public void removeSubCategory(SubCategory subCategory)
	{
		subCategory.setCategory(null);
		if (subcategories.contains(subCategory))
		{
			subcategories.remove(subCategory);
		}
	}

	public void swapSubCategory(int from, int to)
	{
		subcategories.get(from).setCategoryIndex(to);
		subcategories.get(to).setCategoryIndex(from);
		Collections.swap(subcategories, from, to);
	}

	public void swapSubCategory(SubCategory from, SubCategory to)
	{
		int i = from.getCategoryIndex();
		from.setCategoryIndex(to.getCategoryIndex());
		to.setCategoryIndex(i);
		Collections.swap(subcategories, subcategories.indexOf(from), subcategories.indexOf(to));
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
		return (getId() != null ? getId().hashCode() : 0);
	}

	public String toString()
	{
		return getName();
	}
}