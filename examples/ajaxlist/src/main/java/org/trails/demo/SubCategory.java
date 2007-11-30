package org.trails.demo;

import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.*;

@Entity
@ClassDescriptor(hidden = true)
public class SubCategory
{
	private Integer id;
	private String name;
	private Category category;
	private int categoryIndex;

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

	@ManyToOne
	@PropertyDescriptor(readOnly = true)
	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	@PropertyDescriptor(hidden = true)
	@Column(name = "CATEGORY_INDEX")
	public int getCategoryIndex()
	{
		return categoryIndex;
	}

	public void setCategoryIndex(int categoryIndex)
	{
		this.categoryIndex = categoryIndex;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SubCategory subCategory = (SubCategory) o;

		return !(getId() != null ? !getId().equals(subCategory.getId()) : subCategory.getId() != null);

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