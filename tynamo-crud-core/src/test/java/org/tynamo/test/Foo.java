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
package org.tynamo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Foo
{

	private Integer id;
	private boolean primitive;
	private String multiWordProperty;

	private String name;
	private Double number;
	private String readOnly;
	private String hidden;

	private String fromFormula;

	private Date date;
	private Bar bar;
	private Set<Baz> bazzes = new HashSet<Baz>();
	private List<Bing> bings = new ArrayList<Bing>();

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
	 * @return
	 */
	public Double getNumber()
	{
		return number;
	}

	public void setNumber(Double number)
	{
		this.number = number;
	}

	/**
	 * @return
	 */
	public String getMultiWordProperty()
	{
		return multiWordProperty;
	}

	public void setMultiWordProperty(String value)
	{
		this.multiWordProperty = value;
	}

	public Set<Baz> getBazzes()
	{
		return bazzes;
	}

	/**
	 * @param bazzes The bazzes to set.
	 */
	public void setBazzes(Set<Baz> bazzes)
	{
		this.bazzes = bazzes;
	}

	public List<Bing> getBings()
	{
		return bings;
	}

	/**
	 * @param bings The bings to set.
	 */
	public void setBings(List<Bing> bings)
	{
		this.bings = bings;
	}

	public void addBaz(Baz baz)
	{
		getBazzes().add(baz);
	}

	public void removeBaz(Baz baz)
	{
		getBazzes().remove(baz);
	}

	/**
	 * @return
	 */
	public void doSomething()
	{
		// TODO Auto-generated method stub
		setName("something done");
	}

	/**
	 * @return Returns the primitive.
	 *         type="yes_no"
	 */
	public boolean isPrimitive()
	{
		return primitive;
	}

	/**
	 * @param primitive The primitive to set.
	 */
	public void setPrimitive(boolean primitive)
	{
		this.primitive = primitive;
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

	/**
	 * @return Returns the hidden.
	 *         <p/>
	 *         hidden="true"
	 */
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

	/**
	 * readOnly="true"
	 * insert="false" update="false"
	 *
	 * @return Returns the readOnly.
	 */
	public String getReadOnly()
	{
		return readOnly;
	}

	public void setReadOnly(String readOnly)
	{
		this.readOnly = readOnly;
	}

	public Bar getBar()
	{
		return bar;
	}

	public void setBar(Bar bar)
	{
		this.bar = bar;
	}

	public Baz createBaz()
	{
		Baz baz = new Baz();
		baz.setFoo(this);
		return baz;
	}

	public String getFromFormula()
	{
		return fromFormula;
	}

	public void setFromFormula(String fromFormula)
	{
		this.fromFormula = fromFormula;
	}
}
