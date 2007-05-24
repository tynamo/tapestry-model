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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.TrailsRuntimeException;
import org.trails.component.Utils;

/**
 * @author fus8882
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CollectionDescriptor implements IDescriptorExtension,
		IExpressionSupport {
	protected static final Log LOG = LogFactory
			.getLog(CollectionDescriptor.class);

	private Class beanType;

	private Class propertyType;

	private Class elementType;

	private String elementProperty;

	private boolean hidden = true;

	private boolean searchable = true;

	private IPropertyDescriptor propertyDescriptor = null;

	private boolean childRelationship = false;

	private String inverseProperty = null;

	private boolean oneToMany = false;

	public CollectionDescriptor(Class beanType,
			IPropertyDescriptor propertyDescriptor, Class elementType) {
		this(beanType, propertyDescriptor);
		this.elementType = elementType;
		this.elementProperty = propertyDescriptor.getName();
	}

	/**
	 * 
	 * @param beanType
	 * @param propertyDescriptor
	 */
	public CollectionDescriptor(Class beanType,
			IPropertyDescriptor propertyDescriptor) {
		this.beanType = beanType;
		this.propertyType = propertyDescriptor.getPropertyType();
		this.propertyDescriptor = propertyDescriptor;
	}

	/**
	 * 
	 * @param dto
	 */
	public CollectionDescriptor(CollectionDescriptor dto) {
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
	 * @see org.trails.descriptor.PropertyDescriptor#isCollection()
	 */
	public boolean isCollection() {
		return true;
	}

	public String getInverseProperty() {
		return inverseProperty;
	}

	public void setInverseProperty(String inverseProperty) {
		this.inverseProperty = inverseProperty;
	}

	/**
	 * Is this a OneToMany collection? or a ManyToMany collection?
	 * 
	 */
	public boolean isOneToMany() {
		return oneToMany;
	}

	public void setOneToMany(boolean oneToMany) {
		this.oneToMany = oneToMany;
	}

	/**
	 * @return Returns the childRelationship.
	 */
	public boolean isChildRelationship() {
		return childRelationship;
	}

	/**
	 * @param childRelationship
	 *            The childRelationship to set.
	 */
	public void setChildRelationship(boolean childRelationship) {
		this.childRelationship = childRelationship;
		if (this.childRelationship) {
			setSearchable(false);
		}
	}

	public Class getElementType() {
		return elementType;
	}

	public void setElementType(Class elementType) {
		this.elementType = elementType;
	}

	/**
	 * Overrides
	 */
	@Override
	public Object clone() {
		return new CollectionDescriptor(this);
	}

	public void copyFrom(IDescriptor descriptor) {
		LOG.debug("Cloning CollectionDescriptor");
		try {
			BeanUtils.copyProperties(this, (CollectionDescriptor)descriptor);
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
		return getExpression("add");
	}

	public String findRemoveExpression() {
		return getExpression("remove");
	}

	/**
	 * @param method
	 *            the method to look for, usually add or remove
	 * @return the ogln expression to use to add or remove a member to the
	 *         collection. Will look for a addName method where Name is the
	 *         unqualified element class name, if there isn't one it will use
	 *         the collection's add method.
	 */
	public String getExpression(String method) {
		Method addMethod = null;

		try {
			addMethod = getBeanType().getMethod(
					method + Utils.unqualify(getPropertyType().getName()),
					new Class[] { getPropertyType() });
		} catch (NoSuchMethodException ex) {
			// if we don't have one...
			return getPropertyDescriptor().getName() + "." + method;
		} catch (Exception e) {
			throw new TrailsRuntimeException(e);
		}

		return addMethod.getName();
	}

	public Class getBeanType() {
		return beanType;
	}

	public void setBeanType(Class beanType) {
		this.beanType = beanType;
	}

	/**
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

	public String getElementProperty() {
		return elementProperty;
	}

	public void setElementProperty(String mappingProperty) {
		this.elementProperty = mappingProperty;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
