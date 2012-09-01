/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.tynamo.test;

import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Indexed
public class Bar implements IBar
{

	@DocumentId
	private Integer id;

	@Field(index = Index.YES, store = Store.NO)
	@Boost(value = 2.0f) // boosts the importance of the name when searching	
	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the id.
	 */
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

	public String getTransientProperty()
	{
		return "Hello World";
	}
}
