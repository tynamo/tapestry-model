/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.demo;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.trails.descriptor.annotation.PropertyDescriptor;


/**
 * @hibernate.class table="PRODUCT"
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
public class Product
{
	private String name;
	private String description;
	private Date bornOnDate;
	private Integer id;
	private Double cost;

	public Product(String name)
	{
		this.name = name;
	}

	public Product()
	{
	}

	/**
	 * @hibernate.property
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @hibernate.property
	 */
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
	 * @hibernate.property type="date"
	 */
	@PropertyDescriptor(format = "MM/dd/yyyy")
	public Date getBornOnDate()
	{
		return bornOnDate;
	}

	/**
	 * @param bornOnDate The bornOnDate to set.
	 */
	public void setBornOnDate(Date bornOnDate)
	{
		this.bornOnDate = bornOnDate;
	}

	/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
	public String toString()
	{
		return getName();
	}

	/**
	 * @hibernate.id generator-class="native"
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId()
	{
		return id;
	}

	/**
	 * @param number The number to set.
	 */
	public void setId(Integer number)
	{
		this.id = number;
	}

	public void blork()
	{
		description = "blork";
	}

	/**
	 * @return Returns the cost.
	 * @hibernate.property
	 */

	public Double getCost()
	{
		return cost;
	}

	/**
	 * @param cost The cost to set.
	 */
	public void setCost(Double cost)
	{
		this.cost = cost;
	}


	/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
	public boolean equals(Object obj)
	{
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
