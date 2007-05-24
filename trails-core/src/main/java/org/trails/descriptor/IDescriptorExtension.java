package org.trails.descriptor;

import java.io.Serializable;

public interface IDescriptorExtension extends Serializable, Cloneable  {
	public Class getBeanType();

	public void setBeanType(Class propertyType);

	public Class getPropertyType();

	public void setPropertyType(Class propertyType);

	public boolean isSearchable();

	public void setSearchable(boolean searchable);

	public boolean isHidden();

	public void setHidden(boolean hidden);

	public IPropertyDescriptor getPropertyDescriptor();

	public void setPropertyDescriptor(IPropertyDescriptor propertyDescriptor);

	public void copyFrom(IDescriptor descriptor);

	public Object clone();
}
