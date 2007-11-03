package org.trails.demo;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.*;

@Entity
public class Note
{
	private Integer id;
	private String note;
	private Category category;
	private SubCategory subcategory;

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
	@PropertyDescriptor(index = 0)
	@Length(max = 2000)
	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	@ManyToOne
	@PropertyDescriptor(index = 1)
	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	@ManyToOne
	@PropertyDescriptor(index = 2)
	public SubCategory getSubcategory()
	{
		return subcategory;
	}

	public void setSubcategory(SubCategory subcategory)
	{
		this.subcategory = subcategory;
	}

	public String toString()
	{
		return note;
	}
}
