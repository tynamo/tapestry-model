/**
 *
 */
package org.tynamo.descriptor.annotation;

import java.util.Date;
import java.util.Set;


@ClassDescriptor(displayName = "This is annotated", hidden = true, hasCyclicRelationships = true)
public class Annotated
{
	public static final String CLASS_LABEL = "This is annotated";
	public static final String NOT_BLOPPITY_LABEL = "Bloppity";
	public static final String BOOLEAN_LABEL = "True or False";

	private Date date;

	private Set stuff;

	@PropertyDescriptor(displayName = "Bloppity", index = 2)
	private String notBloppity;

	String hidden;

	private String validatedString;

	private boolean booleanProperty;

	@Collection(child = true, inverse = "annotated")
	public Set getStuff()
	{
		return stuff;
	}


	public void setStuff(Set stuff)
	{
		this.stuff = stuff;
	}


	@PropertyDescriptor(hidden = true)
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

	@PropertyDescriptor(displayName = "validated label", index = 3)
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

	@PropertyDescriptor(displayName = "True or False")
	public boolean isBooleanProperty()
	{
		return booleanProperty;
	}


	public void setBooleanProperty(boolean booleanProperty)
	{
		this.booleanProperty = booleanProperty;
	}

}