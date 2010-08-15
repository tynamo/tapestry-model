package org.tynamo.descriptor.extension;


public class EnumReferenceDescriptor implements DescriptorExtension
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
