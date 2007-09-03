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
package org.trails.descriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.TrailsRuntimeException;


public class CollectionDescriptor extends TrailsPropertyDescriptor
{
	protected static final Log LOG = LogFactory.getLog(CollectionDescriptor.class);

	private Class elementType;

	private boolean childRelationship = false;

	private String inverseProperty = null;

	private boolean oneToMany = false;

	public CollectionDescriptor(Class beanType, IPropertyDescriptor descriptor)
	{
		super(beanType, descriptor);
	}

	public CollectionDescriptor(Class beanType, CollectionDescriptor collectionDescriptor)
	{
		super(beanType, collectionDescriptor.getBeanType());
		this.copyFrom(collectionDescriptor);
	}

	public CollectionDescriptor(Class beanType, Class type)
	{
		super(beanType, type);
		// TODO Auto-generated constructor stub
	}

	public CollectionDescriptor(Class beanType, String name, Class type)
	{
		this(beanType, type);
		this.setName(name);
	}

	/* (non-Javadoc)
		 * @see org.trails.descriptor.PropertyDescriptor#isCollection()
		 */
	public boolean isCollection()
	{
		return true;
	}

	/**
	 * @return Returns the elementType.
	 */
	public Class getElementType()
	{
		return elementType;
	}

	/**
	 * @param elementType The elementType to set.
	 */
	public void setElementType(Class elementType)
	{
		this.elementType = elementType;
	}

	public String getInverseProperty()
	{
		return inverseProperty;
	}

	public void setInverseProperty(String inverseProperty)
	{
		this.inverseProperty = inverseProperty;
	}

	/**
	 * Is this a OneToMany collection? or a ManyToMany collection?
	 */
	public boolean isOneToMany()
	{
		return oneToMany;
	}

	public void setOneToMany(boolean oneToMany)
	{
		this.oneToMany = oneToMany;
	}


	/**
	 * @return Returns the childRelationship.
	 */
	public boolean isChildRelationship()
	{
		return childRelationship;
	}

	/**
	 * @param childRelationship The childRelationship to set.
	 */
	public void setChildRelationship(boolean childRelationship)
	{
		this.childRelationship = childRelationship;
		if (this.childRelationship)
		{
			setSearchable(false);
		}
	}

	public Object clone()
	{
		return new CollectionDescriptor(getBeanType(), this);
	}

	public String findAddExpression()
	{
		final String method = "add";

		/**
		 * Awful patch for TRAILS-78 bug.
		 * It evaluates if the object is in the list before adding it.
		 * If it is already there then do nothing else add it.
		 * eg: "bazzes.contains(#member) ? bazzes.size() : bazzes.add" 
		 *
		 */
		if (isChildRelationship() && List.class.isAssignableFrom(getType())) {
			return findExpression(method, getName() + ".contains(#member) ? " + getName() + ".size() : " + getName() + "." + method);
		} else {
			return findExpression(method);
		}

	}

	public String findRemoveExpression()
	{
		return findExpression("remove");
	}

	/**
	 * @param method the method to look for, usually add or remove
	 * @return the ogln expression to use to add or remove a member to the
	 *         collection.  Will look for a addName method where Name is
	 *         the unqualified element class name, if there isn't one it will use
	 *         the collection's add method.
	 */
	private String findExpression(String method, String defaultValue)
	{
		Method addMethod = null;

		try
		{
			addMethod = getBeanType().getMethod(method + getElementType().getSimpleName(), new Class[]{getElementType()});
		} catch (NoSuchMethodException ex)
		{
			// if we don't have one...
			return defaultValue;
		} catch (Exception e)
		{
			throw new TrailsRuntimeException(e);
		}

		return addMethod.getName();
	}

	String findExpression(String method)
	{
		return findExpression(method, getName() + "." + method);
	}

	private void copyFrom(CollectionDescriptor collectionDescriptor)
	{
		LOG.debug("Clonning CollectionDescriptor");
		try
		{
			BeanUtils.copyProperties(this, collectionDescriptor);
		} catch (IllegalAccessException e)
		{
			LOG.error(e.getMessage());
		} catch (InvocationTargetException e)
		{
			LOG.error(e.getMessage());
		}
	}
}
