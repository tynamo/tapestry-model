package org.trails.descriptor;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EnumReferenceDescriptor implements IDescriptorExtension,
		IExpressionSupport {
	protected static final Log LOG = LogFactory
			.getLog(EnumReferenceDescriptor.class);

	private Class beanType;

	private Class propertyType;

	private boolean hidden = true;

	private boolean searchable = true;

	private IPropertyDescriptor propertyDescriptor = null;

	/**
	 * 
	 * @param beanType
	 * @param propertyDescriptor
	 */
	public EnumReferenceDescriptor(Class beanType,
			IPropertyDescriptor propertyDescriptor) {
		this.beanType = beanType;
		this.propertyType = propertyDescriptor.getPropertyType();
		this.propertyDescriptor = propertyDescriptor;
	}

	/**
	 * 
	 * @param dto
	 */
	public EnumReferenceDescriptor(EnumReferenceDescriptor dto) {
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

	public boolean isEnumReference() {
		return true;
	}

	/**
	 * Overrides
	 */
	@Override
	public Object clone() {
		return new EnumReferenceDescriptor(this);
	}

	public void copyFrom (IDescriptor descriptor) {

		try {
			BeanUtils.copyProperties(this, (EnumReferenceDescriptor)descriptor);
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
