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
package org.tynamo.test;


public class Bing
{
	private Integer id;
	private String description;
	private String excludeMe;
	private Integer number;

	/**
	 * @return
	 */
	public Integer getNumber()
	{
		return number;
	}

	public void setNumber(Integer number)
	{
		this.number = number;
	}

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

	public void shouldExclude()
	{
	}

	/**
	 * @return Returns the excludeMe.
	 */
	public String getExcludeMe()
	{
		return excludeMe;
	}

	/**
	 * @param excludeMe The excludeMe to set.
	 */
	public void setExcludeMe(String excludeMe)
	{
		this.excludeMe = excludeMe;
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getDescription();
	}
}
