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
package org.tynamo.examples.simple.entities;

import org.tynamo.descriptor.annotation.PropertyDescriptor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


@Entity
public class Product
{

	private Integer id;
	private String name;
	private String description;
	private Date bornOnDate;
	private Double cost;

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

	@Temporal(TemporalType.DATE)
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

	@Override
	public String toString()
	{
		return getName();
	}

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


	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Product product = (Product) o;

		return getId() != null ? getId().equals(product.getId()) : product.getId() == null;

	}

	@Override
	public int hashCode()
	{
		return (getId() != null ? getId().hashCode() : 0);
	}
}
