package org.trails.descriptor.extension;

import org.trails.descriptor.IDescriptorExtension;


public class EnumReferenceDescriptor implements IDescriptorExtension
{

	private Class actualType;

	public EnumReferenceDescriptor(Class actualType)
	{
		this.actualType = actualType;

	}

	public Class getPropertyType()
	{
		return actualType;
	}

	public boolean isEnumReference()
	{
		return true;
	}
}
