package org.trails.compass.sample.library;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.Embeddable;

@Embeddable
@Searchable(root = false)
public class Name
{

	private String title;

	private String firstName;

	private String lastName;

	public Name()
	{
	}

	public Name(String title, String firstName, String lastName)
	{
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@NotNull
	@PropertyDescriptor(index = 1)
	@SearchableProperty
	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	@NotNull
	@PropertyDescriptor(index = 2)
	@SearchableProperty
	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	@NotNull
	@Length(max = 5)
	@PropertyDescriptor(index = 0)
	@SearchableProperty
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}

		if (!(other instanceof Name))
		{
			return false;
		}

		Name otherName = (Name) other;
		if (title != null)
		{
			if (!title.equals(otherName.getTitle()))
			{
				return false;
			}
		}
		if (firstName != null)
		{
			if (!firstName.equals(otherName.getFirstName()))
			{
				return false;
			}
		}
		if (lastName != null)
		{
			if (!lastName.equals(otherName.getLastName()))
			{
				return false;
			}
		}
		return true;
	}

	public int hashCode()
	{
		int hash = 1;
		hash = hash * 31 + title == null ? 0 : title.hashCode();
		hash = hash * 31 + firstName == null ? 0 : firstName.hashCode();
		hash = hash * 31 + lastName == null ? 0 : lastName.hashCode();
		return hash;
	}

	public String toString()
	{
		return title + " " + " " + firstName + " " + lastName;
	}
}
