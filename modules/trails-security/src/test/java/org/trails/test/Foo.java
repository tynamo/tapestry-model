/*
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
package org.trails.test;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Foo
{

	private Integer id;
	private String name;
	private Date date;
	private IBar bar;

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
	 * @return
	 */
	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{

		return getName();
	}

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Bar.class)
	public IBar getBar()
	{
		return bar;
	}

	public void setBar(IBar bar)
	{
		this.bar = bar;
	}
}
