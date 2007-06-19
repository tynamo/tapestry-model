package org.trails.compass.sample.library;

import org.compass.annotations.*;
import org.hibernate.validator.Length;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Searchable
//(root = false)
public class Article
{

	private Long id;

	private String title;

	private Date publishDate;

	private String summary;

	private String content;

	public Article()
	{
	}

	public Article(String title, Date publishDate, String summary, String content)
	{
		this.title = title;
		this.publishDate = publishDate;
		this.content = content;
		this.summary = summary;
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

	@SearchableProperty
	@SearchableMetaData(name = "content", store = Store.COMPRESS)
	@PropertyDescriptor(summary = false, index = 4)//, richText = true)
	@Length(max = 2500)
	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}


	@SearchableProperty
	@PropertyDescriptor(index = 2, format = "yyyy-MM-dd")
	public Date getPublishDate()
	{
		return publishDate;
	}

	public void setPublishDate(Date date)
	{
		this.publishDate = date;
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

	@SearchableProperty
	@Length(max = 250)
	@PropertyDescriptor(index = 3)
	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String toString()
	{
		return title;
	}


	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || !(o instanceof Article)) return false;

		Article article = (Article) o;

		if (id != null ? !id.equals(article.id) : article.id != null) return false;

		return true;
	}

	public int hashCode()
	{
		return (id != null ? id.hashCode() : 0);
	}
}
