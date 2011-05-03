package org.tynamo.descriptor.annotation;

import java.util.Date;
import java.util.Set;


@ClassDescriptor(nonVisual = true, hasCyclicRelationships = true)
public class Annotated
{
	private Date date;

	private Set stuff;

	@PropertyDescriptor(readOnly = true)
	private String notBloppity;

	String hidden;

	private String validatedString;

	private boolean booleanProperty;

	@CollectionDescriptor(child = true, inverse = "annotated")
	public Set getStuff()
	{
		return stuff;
	}


	public void setStuff(Set stuff)
	{
		this.stuff = stuff;
	}


	@PropertyDescriptor(nonVisual = true)
	public String getHidden()
	{
		return hidden;
	}


	public void setHidden(String hidden)
	{
		this.hidden = hidden;
	}


	public String getNotBloppity()
	{
		return notBloppity;
	}


	public void setNotBloppity(String notBloppity)
	{
		this.notBloppity = notBloppity;
	}

	public String getValidatedString()
	{
		return validatedString;
	}


	public void setValidatedString(String validatedString)
	{
		this.validatedString = validatedString;
	}

	@PropertyDescriptor(format = "MM/dd/yyyy")
	public Date getDate()
	{
		return date;
	}


	public void setDate(Date date)
	{
		this.date = date;
	}

	public boolean isBooleanProperty()
	{
		return booleanProperty;
	}


	public void setBooleanProperty(boolean booleanProperty)
	{
		this.booleanProperty = booleanProperty;
	}

}