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
import org.trails.hibernate.HibernateDescriptorDecorator;

/**
 * @author Chris Nelson
 * 
 * This class represents a to-one association and is created by
 * HibernateDescriptorDecorator
 * 
 * @see HibernateDescriptorDecorator
 */
public class ObjectReferenceDescriptor implements IDescriptorExtension,
		IExpressionSupport {
	protected static final Log LOG = LogFactory
			.getLog(ObjectReferenceDescriptor.class);

	private Class propertyType;

	private Class beanType;

	private IPropertyDescriptor propertyDescriptor = null;

	private boolean hidden = true;

	private boolean searchable = true;

	private String inverseProperty = null;

	private boolean oneToOne = false;

	/**
	 * 
	 * @param beanType
	 * @param propertyDescriptor
	 */
	public ObjectReferenceDescriptor(Class beanType,
			IPropertyDescriptor propertyDescriptor) {
		this.beanType = beanType;
		this.propertyType = propertyDescriptor.getPropertyType();
		this.propertyDescriptor = propertyDescriptor;
	}

	/**
	 * 
	 * @param dto
	 */
	public ObjectReferenceDescriptor(ObjectReferenceDescriptor dto) {
		try {
			BeanUtils.copyProperties(this, dto);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.trails.descriptor.PropertyDescriptor#isObjectReference()
	 */
	public boolean isObjectReference() {
		return true;
	}

	public String getInverseProperty() {
		return inverseProperty;
	}

	public void setInverseProperty(String inverseProperty) {
		this.inverseProperty = inverseProperty;
	}

	public boolean isOneToOne() {
		return oneToOne;
	}

	public void setOneToOne(boolean oneToOne) {
		this.oneToOne = oneToOne;
	}

	/**
	 * Overrides
	 */
	@Override
	public Object clone() {
		return new ObjectReferenceDescriptor(this);
	}

	public void copyFrom (IDescriptor descriptor) {

		try {
			BeanUtils.copyProperties(this, (ObjectReferenceDescriptor)descriptor);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Interface Implementation
	 */

	public String findAddExpression() {
		return getExpression("");
	}

	public String findRemoveExpression() {
		return getExpression("");
	}

	public String getExpression(String method) {
		return getPropertyDescriptor().getName();
	}

	public Class getBeanType() {
		return beanType;
	}

	public void setBeanType(Class beanType) {
		this.beanType = beanType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.trails.descriptor.PropertyDescriptor#getPropertyType()
	 */
	public Class getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Class propertyType) {
		this.propertyType = propertyType;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public IPropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	public void setPropertyDescriptor(IPropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
