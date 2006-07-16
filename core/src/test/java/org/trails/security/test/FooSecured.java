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

import org.trails.security.RestrictionType;
import org.trails.security.annotation.Restriction;
import org.trails.security.annotation.Security;

@Security(restrictions = {@Restriction(restrictionType = RestrictionType.UPDATE, requiredRole="admin"),
		  @Restriction(restrictionType = RestrictionType.VIEW, requiredRole="root")})
@Entity
public class FooSecured {

	private int id;
	private String name;
	private String fooField;
	
	public String getFooField() {
		return fooField;
	}
	public void setFooField(String fooField) {
		this.fooField = fooField;
	}
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Security(restrictions = {@Restriction(restrictionType = RestrictionType.VIEW, requiredRole="admin")})
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
