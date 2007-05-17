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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.trails.validation.ValidateUniqueness;


/**
 * @hibernate.class TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@ValidateUniqueness(property = "description")
public class Baz
{
	private Integer id;
	private String description;

	private Foo foo;

	/**
	 * @hibernate.property
	 */
	@NotNull
	@Length(max = 10, message = "was too long.")
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
	 * @hibernate.id generator-class="native"
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	public void doSomething()
	{
		description = "something done";
	}

	public void doSomethingElse(String something)
	{
		description = something;
	}

	public String toString()
	{
		return getDescription();
	}

	@ManyToOne
	public Foo getFoo()
	{
		return foo;
	}

	public void setFoo(Foo foo)
	{
		this.foo = foo;
	}
}
