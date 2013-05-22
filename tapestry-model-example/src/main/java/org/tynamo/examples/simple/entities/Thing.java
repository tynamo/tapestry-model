package org.tynamo.examples.simple.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.tynamo.PageType;
import org.tynamo.descriptor.annotation.MethodDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.BeanModel;
import org.tynamo.descriptor.annotation.beaneditor.BeanModels;

@Entity
@Table(name = "things", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@BeanModels({
		@BeanModel(pageType = PageType.LIST, exclude = "id, text")
})
@Indexed
public class Thing
{
	private Integer id;

	private String name;

	private String text;

	private Integer number;

	private Integer number2;

	private boolean flag;

	@Id
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

	/**
	 * @return Returns the name.
	 */
	@Field
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

	/**
	 * @return Returns the on.
	 */
	public boolean isFlag()
	{
		return flag;
	}

	/**
	 * @param on The on to set.
	 */
	public void setFlag(boolean on)
	{
		this.flag = on;
	}

	@Column(length = 300)
	@Field
	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Thing thing = (Thing) o;

		return getId() != null ? getId().equals(thing.getId()) : thing.getId() == null;

	}

	@Override
	public int hashCode()
	{
		return (getId() != null ? getId().hashCode() : 0);
	}

	public String toString()
	{
		return getName();
	}

	public Integer getNumber()
	{
		return number;
	}

	public void setNumber(Integer number)
	{
		this.number = number;
	}

	public Integer getNumber2()
	{
		return number2;
	}

	public void setNumber2(Integer number2)
	{
		this.number2 = number2;
	}

	@MethodDescriptor
	public void weirdOperation()
	{
		try
		{
			setNumber2(getNumber() * getNumber2());
		} catch (RuntimeException e)
		{
			setNumber2(0);
		}
	}

}
