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
import java.util.ArrayList;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.beanutils.BeanUtils;
import org.trails.component.Utils;

/**
 * This represents all the Trails metadata for a single class.
 */
public class TrailsClassDescriptor extends TrailsDescriptor implements
	IClassDescriptor
{
	private List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();

	private List<IMethodDescriptor> methodDescriptors = new ArrayList<IMethodDescriptor>();

	// private BeanDescriptor beanDescriptor;
	private boolean child;

	boolean hasCyclicRelationships;

	boolean allowRemove = true;

	boolean allowSave = true;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This is a copy constructor. These need to be clonable for the security
	 * aspect to be able to copy them, so if new properties are added they
	 * should be added here too.
	 */
	public TrailsClassDescriptor(IClassDescriptor descriptor)
	{
		super(descriptor);
		copyPropertyDescriptorsFrom(descriptor);
		copyMethodDescriptorsFrom(descriptor);
	}

	public TrailsClassDescriptor(Class type)
	{
		super(type);
	}

	public TrailsClassDescriptor(Class type, String displayName)
	{
		super(type);
		this.setDisplayName(displayName);
	}

	/**
	 * @param dto
	 */
	public TrailsClassDescriptor(TrailsClassDescriptor dto)
	{
		super(dto);

		try
		{
			BeanUtils.copyProperties(this, dto);
		} catch (IllegalAccessException e)
		{
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e)
		{
			LOG.error(e.toString());
			e.printStackTrace();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	private void copyMethodDescriptorsFrom(IClassDescriptor descriptor)
	{
		for (IMethodDescriptor methodDescriptor : descriptor
			.getMethodDescriptors())
		{
			getMethodDescriptors().add(
				IMethodDescriptor.class.cast(methodDescriptor.clone()));
		}
	}

	protected void copyPropertyDescriptorsFrom(IClassDescriptor descriptor)
	{
		for (IPropertyDescriptor iPropertyDescriptor : descriptor
			.getPropertyDescriptors())
		{
			getPropertyDescriptors()
				.add(
					IPropertyDescriptor.class.cast(iPropertyDescriptor
						.clone()));
		}
	}

	/**
	 * @param ognl
	 * @return
	 */
	private IPropertyDescriptor findDescriptor(String ognl)
	{
		try
		{
			return (IPropertyDescriptor) Ognl.getValue(ognl, this);
		} catch (OgnlException oe)
		{
			// oe.printStackTrace();

			return null;
		} catch (IndexOutOfBoundsException ie)
		{
			return null;
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public IPropertyDescriptor getPropertyDescriptor(String name)
	{
		return findDescriptor("propertyDescriptors.{? name == '" + name
			+ "'}[0]");
	}

	/**
	 * @return
	 */
	public String getPluralDisplayName()
	{
		return Utils.pluralize(Utils.unCamelCase(getDisplayName()));
	}

	public List getPropertyDescriptors(String[] properties)
	{
		ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		for (String property : properties)
		{
			descriptors.add(getPropertyDescriptor(property));
		}
		return descriptors;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// bean getters / setters
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return Returns the methodDescriptors.
	 */
	public List<IMethodDescriptor> getMethodDescriptors()
	{
		return methodDescriptors;
	}

	/**
	 * @param methodDescriptors The methodDescriptors to set.
	 */
	public void setMethodDescriptors(List<IMethodDescriptor> methodDescriptors)
	{
		this.methodDescriptors = methodDescriptors;
	}

	/**
	 * @return Returns the propertyDescriptors.
	 */
	public List<IPropertyDescriptor> getPropertyDescriptors()
	{
		return propertyDescriptors;
	}

	/**
	 * @param propertyDescriptors The propertyDescriptors to set.
	 */
	public void setPropertyDescriptors(
		List<IPropertyDescriptor> propertyDescriptors)
	{
		this.propertyDescriptors = propertyDescriptors;
	}

	public IPropertyDescriptor getIdentifierDescriptor()
	{
		String ognl = "propertyDescriptors.{? identifier}[0]";

		return findDescriptor(ognl);
	}

	/**
	 * @return Returns the child.
	 */
	public boolean isChild()
	{
		return child;
	}

	/**
	 * @param child The child to set.
	 */
	public void setChild(boolean child)
	{
		this.child = child;
	}

	@Override
	public Object clone()
	{
		return new TrailsClassDescriptor(this);
	}

	@Override
	public void copyFrom(IDescriptor descriptor)
	{
		super.copyFrom(descriptor);

		if (descriptor instanceof TrailsClassDescriptor)
		{

			try
			{
				BeanUtils.copyProperties(this,
					(TrailsClassDescriptor) descriptor);
				copyPropertyDescriptorsFrom((TrailsClassDescriptor) descriptor);
				copyMethodDescriptorsFrom((TrailsClassDescriptor) descriptor);
			} catch (IllegalAccessException e)
			{
				LOG.error(e.getMessage());
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				LOG.error(e.getMessage());
				e.printStackTrace();
			} catch (Exception e)
			{
				LOG.error(e.toString());
				e.printStackTrace();
			}
		}
	}

	public boolean isAllowRemove()
	{
		return allowRemove;
	}

	public void setAllowRemove(boolean allowRemove)
	{
		this.allowRemove = allowRemove;
	}

	public boolean isAllowSave()
	{
		return allowSave;
	}

	public void setAllowSave(boolean allowSave)
	{
		this.allowSave = allowSave;
	}

	public boolean getHasCyclicRelationships()
	{
		return hasCyclicRelationships;
	}

	public void setHasCyclicRelationships(boolean hasBidirectionalRelationship)
	{
		this.hasCyclicRelationships = hasBidirectionalRelationship;
	}

	/**
	 * Added toString method to help with unit testing debugging.
	 */
	public String toString()
	{
		return "{TrailsClassDescriptor - Type: " + getType() + "}";
	}

}
