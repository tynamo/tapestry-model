package org.trails.component;

import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.trails.descriptor.BlockFinder;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class SimplePropertyEditor extends PropertyEditor
{

	@Parameter(required = true)
	public abstract String getProperty();

	@Parameter(defaultValue = "container.classDescriptor")
	public abstract IClassDescriptor getClassDescriptor();

	@InjectObject("service:trails.core.EditorService")
	public abstract BlockFinder getBlockFinder();

	public IPropertyDescriptor getDescriptor()
	{
		return getClassDescriptor().getPropertyDescriptor(getProperty());
	}
}