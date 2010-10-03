package org.tynamo.descriptor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tynamo.exception.TynamoRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class CollectionDescriptor extends TynamoPropertyDescriptorImpl
{
	protected static final Log LOG = LogFactory.getLog(CollectionDescriptor.class);

	private Class elementType;

	private boolean childRelationship = false;

	private String inverseProperty = null;

	private boolean oneToMany = false;

	private String addExpression = null;

	private String removeExpression = null;

	private String swapExpression = null;

	private boolean allowRemove = true;

	public CollectionDescriptor(Class beanType, TynamoPropertyDescriptor descriptor)
	{
		super(beanType, descriptor);
	}

	public CollectionDescriptor(Class beanType, CollectionDescriptor collectionDescriptor)
	{
		super(beanType, collectionDescriptor.getBeanType());
		this.copyFrom(collectionDescriptor);
	}

	public CollectionDescriptor(Class beanType, String name, Class type)
	{
		super(beanType, type);
		this.setName(name);
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.descriptor.TynamoPropertyDescriptorImpl#isCollection()
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

	public String getAddExpression()
	{
		if (addExpression == null)
		{
			addExpression = findAddExpression();
		}
		return addExpression;
	}

	public void setAddExpression(String addExpression)
	{
		this.addExpression = addExpression;
	}

	public String getRemoveExpression()
	{
		if (removeExpression == null)
		{
			removeExpression = findRemoveExpression();
		}
		return removeExpression;
	}

	public void setRemoveExpression(String removeExpression)
	{
		this.removeExpression = removeExpression;
	}

	public String getSwapExpression()
	{
		return swapExpression;
	}

	public void setSwapExpression(String swapExpression)
	{
		this.swapExpression = swapExpression;
	}

	public boolean isAllowRemove()
	{
		return allowRemove;
	}

	public void setAllowRemove(boolean allowRemove)
	{
		this.allowRemove = allowRemove;
	}

	public Object clone()
	{
		return new CollectionDescriptor(getBeanType(), this);
	}

	private String findAddExpression()
	{
		final String method = "add";

		/**
		 * Awful patch for TRAILS-78 bug.
		 * It evaluates if the object is in the list before adding it.
		 * If it is already there then do nothing else add it.
		 * eg: "bazzes.contains(#member) ? bazzes.size() : bazzes.add" 
		 *
		 */
		if (isChildRelationship() && List.class.isAssignableFrom(getBeanType()))
		{
			return findExpression(method,
					getName() + ".contains(#member) ? " + getName() + ".size() : " + getName() + "." + method);
		} else
		{
			return findExpression(method);
		}

	}

	private String findRemoveExpression()
	{
		return findExpression("remove");
	}

	/**
	 * @param method the method to look for, usually add or remove
	 * @return the ogln expression to use to add or remove a member to the collection.  Will look for a addName method
	 *         where Name is the unqualified element class name, if there isn't one it will use the collection's add
	 *         method.
	 */
	private String findExpression(String method, String defaultValue)
	{
		Method addMethod = null;

		try
		{
			addMethod =
					getBeanType().getMethod(method + getElementType().getSimpleName(), new Class[]{getElementType()});
		} catch (NoSuchMethodException ex)
		{
			// if we don't have one...
			return defaultValue;
		} catch (Exception e)
		{
			throw new TynamoRuntimeException(e);
		}

		return addMethod.getName();
	}

	String findExpression(String method)
	{
		return findExpression(method, getName() + "." + method);
	}

	private void copyFrom(CollectionDescriptor collectionDescriptor)
	{
		LOG.debug("Cloning CollectionDescriptor");
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
