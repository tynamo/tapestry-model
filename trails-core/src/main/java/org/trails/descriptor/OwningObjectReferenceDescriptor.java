package org.trails.descriptor;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.component.AssociationMgt;
import org.trails.component.AssociationSelect;
import org.trails.hibernate.HibernateDescriptorDecorator;

/**
 * This class represents a one-to-one association and is created by
 * HibernateDescriptorDecorator at bootstrap time
 * 
 * @OneToOne use case.
 * 
 * EXAMPLE: one-to-one association
 * 
 * Organization-<>-----Director
 * 
 * Organization is the owner. Director is the reference.
 * 
 * Each requires a specialized trails property editor.
 * 
 * Both of these objects are manipulated by dedicated descriptors respectively
 * OwningObjectReferenceDescriptor and ObjectReferenceDescriptor which have a
 * sole duty of governing which property editor launches.
 * 
 * In short this guy operates the owning side of the association for the
 * framework and gets detected as owner iff OneToOne does NOT have the mappedBy
 * attribute set
 * 
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 * 
 * @see HibernateDescriptorDecorator
 * @see ObjectReferenceDescriptor
 * @see AssociationSelect
 * @see AssociationMgt
 */
public class OwningObjectReferenceDescriptor implements IDescriptorExtension,
		IExpressionSupport {
	protected static final Log LOG = LogFactory
			.getLog(OwningObjectReferenceDescriptor.class);

	private Class beanType;

	private Class propertyType;

	private boolean hidden = true;

	private boolean searchable = true;

	private IPropertyDescriptor propertyDescriptor = null;

	private String inverseProperty = null;

	private boolean oneToOne = false;

	/**
	 * 
	 * @param beanType
	 * @param propertyDescriptor
	 */
	public OwningObjectReferenceDescriptor(Class beanType, IPropertyDescriptor propertyDescriptor) {
		this.beanType = beanType;
		this.propertyType = propertyDescriptor.getPropertyType();
		this.propertyDescriptor = propertyDescriptor;
	}

	/**
	 * 
	 * @param dto
	 */
	public OwningObjectReferenceDescriptor(OwningObjectReferenceDescriptor dto) {
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

	public String getInverseProperty() {
		return inverseProperty;
	}

	public void setInverseProperty(String inverseProperty) {
		this.inverseProperty = inverseProperty;
	}

	public boolean isOneToOne() {
		return oneToOne;
	}

	/**
	 * @see org.trails.descriptor.PropertyDescriptor#isOwningObjectReference()
	 */
	public boolean isOwningObjectReference() {
		return true;
	}

	public void setOneToOne(boolean oneToOne) {
		this.oneToOne = oneToOne;
	}

	/**
	 * Overrides
	 */
	@Override
	public Object clone() {
		return new OwningObjectReferenceDescriptor(this);
	}

	public void copyFrom (IDescriptor descriptor) {

		try {
			BeanUtils.copyProperties(this, (OwningObjectReferenceDescriptor)descriptor);
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

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
