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
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 * @OneToOne use case.
 * <p/>
 * EXAMPLE: one-to-one association
 * <p/>
 * Organization-<>-----Director
 * <p/>
 * Organization is the owner. Director is the reference.
 * <p/>
 * Each requires a specialized trails property editor.
 * <p/>
 * Both of these objects are manipulated by
 * dedicated descriptors respectively OwningObjectReferenceDescriptor and
 * ObjectReferenceDescriptor which have a sole duty of governing which
 * property editor launches.
 * <p/>
 * In short this guy operates the owning side of the association for the
 * framework and gets detected as owner iff OneToOne does NOT have the mappedBy
 * attribute set
 * @see HibernateDescriptorDecorator
 * @see ObjectReferenceDescriptor
 * @see AssociationSelect
 * @see AssociationMgt
 */
public class OwningObjectReferenceDescriptor extends TrailsPropertyDescriptor
	implements IPropertyDescriptor
{
	protected static final Log LOG = LogFactory
		.getLog(OwningObjectReferenceDescriptor.class);

	private Class actualType;

	private String inverseProperty = null;

	private boolean oneToOne = false;

	/**
	 * @param beanType
	 * @param descriptor
	 * @param actualType
	 */
	public OwningObjectReferenceDescriptor(Class beanType,
										   IPropertyDescriptor descriptor, Class actualType)
	{
		this(beanType, descriptor.getPropertyType(), actualType);
		copyFrom(descriptor);
	}

	/**
	 * @param beanType
	 * @param declaredType
	 * @param actualType
	 */
	public OwningObjectReferenceDescriptor(Class beanType, Class declaredType,
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
	public OwningObjectReferenceDescriptor(Class beanType, String name,
										   Class type)
	{
		super(beanType, name, type);
		this.actualType = type;
	}

	/**
	 * @param dto
	 */
	public OwningObjectReferenceDescriptor(OwningObjectReferenceDescriptor dto)
	{
		super(dto.getBeanType(), dto.getName(), dto.getType());

		try
		{
			BeanUtils.copyProperties(this, dto);
		} catch (Exception e)
		{
			LOG.error(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * @see org.trails.descriptor.PropertyDescriptor#getPropertyType()
	 */
	public Class getPropertyType()
	{
		return actualType;
	}

	/**
	 * @see org.trails.descriptor.PropertyDescriptor#isOwningObjectReference()
	 */
	@Override
	public boolean isOwningObjectReference()
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
		return new OwningObjectReferenceDescriptor(this);
	}

	public String findAddExpression()
	{
		return findExpression();
	}

	public String findRemoveExpression()
	{
		return findExpression();
	}

	/**
	 * This expression is entity oriented
	 *
	 * @return
	 */
	String findExpression()
	{
		return getName();
	}

	private void copyFrom(OwningObjectReferenceDescriptor owd)
	{
		LOG.debug("Cloning OwningObjectReferenceDescriptor");
		try
		{
			BeanUtils.copyProperties(this, owd);
		} catch (IllegalAccessException e)
		{
			LOG.error(e.getMessage());
		} catch (InvocationTargetException e)
		{
			LOG.error(e.getMessage());
		}
	}
}
