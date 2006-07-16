package org.trails.descriptor;

import java.util.List;

public interface DescriptorFactory
{
	public IClassDescriptor buildClassDescriptor(Class type);
	
	public void setMethodExcludes(List methodExcludes);
	
	public void setPropertyExcludes(List propertyExcludes);
}
