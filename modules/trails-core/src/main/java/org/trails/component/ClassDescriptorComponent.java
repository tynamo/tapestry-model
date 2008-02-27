package org.trails.component;

import java.util.List;
import java.util.ArrayList;

import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Block;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

public abstract class ClassDescriptorComponent extends TrailsComponent
{

	@Parameter(required = false, defaultValue = "page.classDescriptor", cache = true)
	public abstract IClassDescriptor getClassDescriptor();

	@Parameter(required = false, defaultValue = "ognl:null", cache = true)
	public abstract List<String> getPropertyNames();

	public List<IPropertyDescriptor> getPropertyDescriptors()
	{
		if (getPropertyNames() == null || getPropertyNames().size() == 0)
		{
			List<IPropertyDescriptor> displayingPropertyDescriptors = new ArrayList<IPropertyDescriptor>();
			for (IPropertyDescriptor propertyDescriptor : getClassDescriptor().getPropertyDescriptors())
			{
				if (shouldDisplay(propertyDescriptor))
				{
					displayingPropertyDescriptors.add(propertyDescriptor);
				}
			}
			return displayingPropertyDescriptors;
		} else
		{
			return getClassDescriptor().getPropertyDescriptors(getPropertyNames());
		}
	}

	/**
	 * Hook method to allow subclasses to modify when an IPropertyDescriptor should be displayed.
	 *
	 * @param descriptor
	 * @return
	 */
	protected boolean shouldDisplay(IPropertyDescriptor descriptor)
	{
		return !descriptor.isHidden();
	}

	public boolean hasBlock(String propertyName)
	{
		return getPage().getComponents().containsKey(propertyName);
	}

	public Block getBlock(String propertyName)
	{
		if (hasBlock(propertyName))
		{
			return (Block) getPage().getComponent(propertyName);
		}
		return null;
	}
}
