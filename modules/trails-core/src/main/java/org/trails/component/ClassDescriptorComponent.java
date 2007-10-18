package org.trails.component;

import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Block;
import org.trails.TrailsRuntimeException;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

public abstract class ClassDescriptorComponent extends TrailsComponent
{

	@Parameter(required = false, defaultValue = "page.classDescriptor", cache = true)
	public abstract IClassDescriptor getClassDescriptor();

	public abstract void setClassDescriptor(IClassDescriptor ClassDescriptor);

	@Parameter(required = false, defaultValue = "ognl:null", cache = true)
	public abstract String[] getPropertyNames();

	public abstract void setPropertyNames(String[] PropertyNames);

	/**
	 * @return
	 * @throws OgnlException
	 */
	public List<IPropertyDescriptor> getPropertyDescriptors()
	{
		if (getPropertyNames() == null || getPropertyNames().length == 0)
		{
			try
			{
				return (List) Ognl.getValue("#this.{? not(hidden)}", getClassDescriptor().getPropertyDescriptors());
			}
			catch (OgnlException oe)
			{
				throw new TrailsRuntimeException(oe, getClassDescriptor().getType());
			}
		} else
		{
			return getClassDescriptor().getPropertyDescriptors(getPropertyNames());
		}
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
