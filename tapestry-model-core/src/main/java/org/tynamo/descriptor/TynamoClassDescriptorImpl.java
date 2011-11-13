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
package org.tynamo.descriptor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * This represents all the Tynamo metadata for a single class.
 */
public class TynamoClassDescriptorImpl extends TynamoDescriptor implements TynamoClassDescriptor
{
	private List<TynamoPropertyDescriptor> propertyDescriptors = new ArrayList<TynamoPropertyDescriptor>();

	private List<IMethodDescriptor> methodDescriptors = new ArrayList<IMethodDescriptor>();

	// private BeanDescriptor beanDescriptor;
	private boolean child;

	boolean hasCyclicRelationships;

	boolean allowRemove = true;

	boolean allowSave = true;

	/**
	 * This is a copy constructor. These need to be clonable for the security
	 * aspect to be able to copy them, so if new properties are added they
	 * should be added here too.
	 */
	public TynamoClassDescriptorImpl(TynamoClassDescriptor descriptor)
	{
		super(descriptor);
		copyPropertyDescriptorsFrom(descriptor);
		copyMethodDescriptorsFrom(descriptor);
	}

	public TynamoClassDescriptorImpl(Class beanType)
	{
		super(beanType);
	}

	/**
	 * @param dto
	 */
	public TynamoClassDescriptorImpl(TynamoClassDescriptorImpl dto)
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

	private void copyMethodDescriptorsFrom(TynamoClassDescriptor descriptor)
	{
		for (IMethodDescriptor methodDescriptor : descriptor.getMethodDescriptors())
		{
			getMethodDescriptors().add(IMethodDescriptor.class.cast(methodDescriptor.clone()));
		}
	}

	protected void copyPropertyDescriptorsFrom(TynamoClassDescriptor descriptor)
	{
		for (TynamoPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors())
		{
			getPropertyDescriptors().add(TynamoPropertyDescriptor.class.cast(propertyDescriptor.clone()));
		}
	}

	public TynamoPropertyDescriptor getPropertyDescriptor(final String name)
	{
		return F.flow(propertyDescriptors).filter(new Predicate<TynamoPropertyDescriptor>()
		{
			public boolean accept(TynamoPropertyDescriptor descriptor)
			{
				return descriptor.getName().equals(name);
			}
		}).first();
	}

	public List<TynamoPropertyDescriptor> getPropertyDescriptors(List<String> properties)
	{
		ArrayList<TynamoPropertyDescriptor> descriptors = new ArrayList<TynamoPropertyDescriptor>();
		for (String property : properties)
		{
			descriptors.add(getPropertyDescriptor(property));
		}
		return descriptors;
	}

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
	public List<TynamoPropertyDescriptor> getPropertyDescriptors()
	{
		return propertyDescriptors;
	}

	/**
	 * @param propertyDescriptors The propertyDescriptors to set.
	 */
	public void setPropertyDescriptors(List<TynamoPropertyDescriptor> propertyDescriptors)
	{
		this.propertyDescriptors = propertyDescriptors;
	}

	public TynamoPropertyDescriptor getIdentifierDescriptor()
	{
		return F.flow(propertyDescriptors).filter(new Predicate<TynamoPropertyDescriptor>()
		{
			public boolean accept(TynamoPropertyDescriptor descriptor)
			{
				return descriptor.isIdentifier();
			}
		}).first();
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
		return new TynamoClassDescriptorImpl(this);
	}

	@Override
	public void copyFrom(Descriptor descriptor)
	{
		super.copyFrom(descriptor);

		if (descriptor instanceof TynamoClassDescriptorImpl)
		{

			try
			{
				BeanUtils.copyProperties(this, (TynamoClassDescriptorImpl) descriptor);
				copyPropertyDescriptorsFrom((TynamoClassDescriptorImpl) descriptor);
				copyMethodDescriptorsFrom((TynamoClassDescriptorImpl) descriptor);
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
		return "{TynamoClassDescriptor - Type: " + getBeanType() + "}";
	}

}
