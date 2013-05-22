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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;

@Table(name = "otherthings", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Entity
public class Thing2
{
	String identifier;
	String name;

	private String title;
	String hidden;

	String readOnly = "foo";

	@Id
	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	@Pattern(regexp = "[a-z]+")
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

	@PropertyDescriptor(nonVisual = true)
	public String getHidden()
	{
		return hidden;
	}

	/**
	 * @param hidden The hidden to set.
	 */
	public void setHidden(String hidden)
	{
		this.hidden = hidden;
	}

	@PropertyDescriptor(readOnly = true)
	public String getReadOnly()
	{
		return readOnly;
	}

	/**
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(String readOnly)
	{
		this.readOnly = readOnly;
	}

	@Pattern(regexp = "[a-z]+")
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
}
