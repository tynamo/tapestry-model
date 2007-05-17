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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.IndexColumn;
import org.trails.validation.AssertNoOrphans;


/**
 * @javabean.class name="Foo"
 * @hibernate.class table="FOO"
 * <p/>
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@AssertNoOrphans(childrenProperty = "bazzes", message = "This is a message")
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

	/**
	 * @javabean.property
	 * @hibernate.id generator-class="assigned"
	 */
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

	/**
	 * @hibernate.property not-null="true"
	 * @javabean.property displayName="The Name"
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
	 * @return
	 * @javabean.property
	 * @hibernate.property
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
	 * @javabean.property
	 * @hibernate.property
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
	 * @javabean.property
	 * @hibernate.property
	 */
	@Column(length = 101)
	public String getMultiWordProperty()
	{
		return multiWordProperty;
	}

	public void setMultiWordProperty(String value)
	{
		this.multiWordProperty = value;
	}

	/**
	 * @javabean.property expert="true"
	 * @hibernate.set cascade="all-delete-orphan"
	 * @hibernate.collection-key column="BAZ_ID"
	 * @hibernate.collection-one-to-many class="org.trails.test.Baz"
	 */

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "foo")
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

	/**
	 * @javabean.property
	 * @hibernate.list
	 * @hibernate.collection-key column="BING_ID"
	 * @hibernate.collection-index column="BING_INDEX"
	 * @hibernate.collection-one-to-many class="org.trails.test.Bing"
	 */
	@OneToMany
	@JoinColumn(name = "FOO_ID")
	@IndexColumn(name = "BING_INDEX")
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

	public void addBing(Bing bing)
	{
		getBings().add(bing);
	}

	public void removeBing(Bing bing)
	{
		getBings().remove(bing);
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
	 * @hibernate.property type="yes_no"
	 * @javabean.property
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


	/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
	public String toString()
	{

		return getName();
	}

	/**
	 * @return Returns the hidden.
	 * @hibernate.property
	 * @javabean.property hidden="true"
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
	 * @return Returns the readOnly.
	 * @javabean.property readOnly="true"
	 * @hibernate.property insert="false" update="false"
	 */
	public String getReadOnly()
	{
		return readOnly;
	}

	public void setReadOnly(String readOnly)
	{
		this.readOnly = readOnly;
	}

	@ManyToOne(cascade = CascadeType.ALL)
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

	@Formula("lower('ABC')")
	public String getFromFormula()
	{
		return fromFormula;
	}

	public void setFromFormula(String fromFormula)
	{
		this.fromFormula = fromFormula;
	}
}
