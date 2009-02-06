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
package org.trailsframework.descriptor;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * This represents all the Trails metadata for a single class.
 */
public class TrailsClassDescriptorImpl extends TrailsDescriptor implements TrailsClassDescriptor
{
	private List<TrailsPropertyDescriptor> propertyDescriptors = new ArrayList<TrailsPropertyDescriptor>();

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
	public TrailsClassDescriptorImpl(TrailsClassDescriptor descriptor)
	{
		super(descriptor);
		copyPropertyDescriptorsFrom(descriptor);
		copyMethodDescriptorsFrom(descriptor);
	}

	public TrailsClassDescriptorImpl(Class type)
	{
		super(type);
	}

	/**
	 * @param dto
	 */
	public TrailsClassDescriptorImpl(TrailsClassDescriptorImpl dto)
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
	private void copyMethodDescriptorsFrom(TrailsClassDescriptor descriptor)
	{
		for (IMethodDescriptor methodDescriptor : descriptor
			.getMethodDescriptors())
		{
			getMethodDescriptors().add(
				IMethodDescriptor.class.cast(methodDescriptor.clone()));
		}
	}

	protected void copyPropertyDescriptorsFrom(TrailsClassDescriptor descriptor)
	{
		for (TrailsPropertyDescriptor iPropertyDescriptor : descriptor
			.getPropertyDescriptors())
		{
			getPropertyDescriptors()
				.add(
					TrailsPropertyDescriptor.class.cast(iPropertyDescriptor
						.clone()));
		}
	}

	/**
	 * @param ognl
	 * @return
	 */
	private TrailsPropertyDescriptor findDescriptor(String ognl)
	{
		try
		{
			return (TrailsPropertyDescriptor) Ognl.getValue(ognl, this);
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
	public TrailsPropertyDescriptor getPropertyDescriptor(String name)
	{
		return findDescriptor("propertyDescriptors.{? name == '" + name + "'}[0]");
	}

	public List<TrailsPropertyDescriptor> getPropertyDescriptors(List<String> properties) {
		ArrayList<TrailsPropertyDescriptor> descriptors = new ArrayList<TrailsPropertyDescriptor>();
		for (String property : properties) {
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
	public List<TrailsPropertyDescriptor> getPropertyDescriptors()
	{
		return propertyDescriptors;
	}

	/**
	 * @param propertyDescriptors The propertyDescriptors to set.
	 */
	public void setPropertyDescriptors(
		List<TrailsPropertyDescriptor> propertyDescriptors)
	{
		this.propertyDescriptors = propertyDescriptors;
	}

	public TrailsPropertyDescriptor getIdentifierDescriptor()
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
		return new TrailsClassDescriptorImpl(this);
	}

	@Override
	public void copyFrom(Descriptor descriptor)
	{
		super.copyFrom(descriptor);

		if (descriptor instanceof TrailsClassDescriptorImpl)
		{

			try
			{
				BeanUtils.copyProperties(this,
					(TrailsClassDescriptorImpl) descriptor);
				copyPropertyDescriptorsFrom((TrailsClassDescriptorImpl) descriptor);
				copyMethodDescriptorsFrom((TrailsClassDescriptorImpl) descriptor);
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
