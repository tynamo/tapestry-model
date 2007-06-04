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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Chris Nelson
 *         <p/>
 *         This class represents a to-one association and is created by
 *         HibernateDescriptorDecorator
 */
public class ObjectReferenceDescriptor extends TrailsPropertyDescriptor
{
	protected static final Log LOG = LogFactory.getLog(ObjectReferenceDescriptor.class);

	private Class actualType;
	private String inverseProperty = null;
	private boolean oneToOne = false;

	public ObjectReferenceDescriptor(Class beanType,
									 IPropertyDescriptor descriptor, Class actualType)
	{
		this(beanType, descriptor.getPropertyType(), actualType);
		copyFrom(descriptor);
	}

	/**
	 * @param realDescriptor
	 */
	public ObjectReferenceDescriptor(Class beanType, Class declaredType,
									 Class actualType)
	{
		super(beanType, declaredType);
		this.actualType = actualType;
	}

	/**
	 * @param beanType
	 * @param name
	 * @param type
	 */
	public ObjectReferenceDescriptor(Class beanType, String name, Class type)
	{
		super(beanType, name, type);
		this.actualType = type;
	}

	/*
		 * (non-Javadoc)
		 *
		 * @see org.trails.descriptor.PropertyDescriptor#getPropertyType()
		 */
	public Class getPropertyType()
	{
		return actualType;
	}

	/*
		 * (non-Javadoc)
		 *
		 * @see org.trails.descriptor.PropertyDescriptor#isObjectReference()
		 */
	public boolean isObjectReference()
	{
		return true;
	}

	public Class getActualType()
	{
		return actualType;
	}

	public void setActualType(Class actualType)
	{
		this.actualType = actualType;
	}

	public String getInverseProperty()
	{
		return inverseProperty;
	}

	public void setInverseProperty(String inverseProperty)
	{
		this.inverseProperty = inverseProperty;
	}

	public boolean isOneToOne()
	{
		return oneToOne;
	}

	public void setOneToOne(boolean oneToOne)
	{
		this.oneToOne = oneToOne;
	}

	public Object clone()
	{
		return new ObjectReferenceDescriptor(getBeanType(), this,
			getPropertyType());
	}

	private void copyFrom(ObjectReferenceDescriptor ord)
	{
		LOG.debug("Cloning ObjectReferenceDescriptor");
		try
		{
			BeanUtils.copyProperties(this, ord);
		} catch (IllegalAccessException e)
		{
			LOG.error(e.getMessage());
		} catch (InvocationTargetException e)
		{
			LOG.error(e.getMessage());
		}
	}
}
