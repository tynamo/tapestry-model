package org.trails.compass.sample.library;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.hibernate.validator.Length;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Searchable
@Entity
public class Book
{

	private Long id;

	private String title;

	private Date publishDate;

	private String summary;

	public Book()
	{
	}


	@SearchableId
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@PropertyDescriptor(index = 0)
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Book(String title, Date publishDate, String summary)
	{
		this.title = title;
		this.publishDate = publishDate;
		this.summary = summary;
	}

	@SearchableProperty
	@PropertyDescriptor(index = 2)
	public Date getPublishDate()
	{
		return publishDate;
	}

	public void setPublishDate(Date publishDate)
	{
		this.publishDate = publishDate;
	}

	@SearchableProperty
	@Length(max = 500)
	@PropertyDescriptor(index = 3)
	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	@SearchableProperty
	@PropertyDescriptor(index = 1)
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String toString()
	{
		return title;
	}


	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || !(o instanceof Book)) return false;

		Book book = (Book) o;

		if (id != null ? !id.equals(book.id) : book.id != null) return false;

		return true;
	}

	public int hashCode()
	{
		return (id != null ? id.hashCode() : 0);
	}
}
