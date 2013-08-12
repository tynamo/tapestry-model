package org.tynamo.descriptor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;


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

	boolean searchable = false;

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
			LOGGER.error(e.getMessage(), e);
		} catch (InvocationTargetException e)
		{
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e)
		{
			LOGGER.error(e.toString(), e);
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
				LOGGER.error(e.getMessage(), e);
			} catch (InvocationTargetException e)
			{
				LOGGER.error(e.getMessage(), e);
			} catch (Exception e)
			{
				LOGGER.error(e.toString(), e);
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

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable)
	{
		this.searchable = searchable;
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
