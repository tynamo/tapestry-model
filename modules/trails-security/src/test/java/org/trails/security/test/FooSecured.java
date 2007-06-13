/*
 * Created on 14/12/2005
 *
 * Copyright 2005 - GWE Software Ltda.
 *
 */
package org.trails.security.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.trails.security.annotation.UpdateRequiresAssociation;
import org.trails.security.annotation.UpdateRequiresRole;
import org.trails.security.annotation.ViewRequiresRole;
import org.trails.security.domain.User;

@UpdateRequiresRole("admin")
@ViewRequiresRole("root")
@UpdateRequiresAssociation("owner")
@Entity
public class FooSecured
{

	private int id;
	private String name;
	private String fooField;
	private User owner;
	

	public String getFooField()
	{
		return fooField;
	}

	public void setFooField(String fooField)
	{
		this.fooField = fooField;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@ViewRequiresRole("admin")
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public User getOwner()
  {
      return owner;
  }
  public void setOwner(User owner)
  {
      this.owner = owner;
  }
}
