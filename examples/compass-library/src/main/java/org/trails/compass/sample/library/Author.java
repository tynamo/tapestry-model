package org.trails.compass.sample.library;

import org.compass.annotations.*;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Searchable
@Entity
public class Author
{

	private Long id; // identifier
	private Name name = new Name();
	private Date birthday;
	private Set<Book> books = new HashSet<Book>();


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

	@Embedded
	@SearchableComponent
	@PropertyDescriptor(index = 1)
	public Name getName()
	{
		return name;
	}

	public void setName(Name name)
	{
		this.name = name;
	}

	@SearchableProperty(name = "birthdayOrig")
	@SearchableMetaData(name = "birthday", format = "yyyy-MM-dd")
	@PropertyDescriptor(index = 2, format = "yyyy-MM-dd")
	public Date getBirthday()
	{
		return this.birthday;
	}

	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}


	@SearchableReference
//    @SearchableComponent
	@ManyToMany(targetEntity = Book.class, cascade = CascadeType.ALL)
	public Set<Book> getBooks()
	{
		return books;
	}

	public void setBooks(Set<Book> books)
	{
		this.books = books;
	}


	public String toString()
	{
		return name.toString();
	}
}
