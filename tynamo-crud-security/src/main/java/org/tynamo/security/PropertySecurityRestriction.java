package org.tynamo.security;

import org.tynamo.descriptor.IClassDescriptor;

public class PropertySecurityRestriction extends SecurityRestriction
{
	private String propertyName;

	public String getPropertyName()
	{
		return propertyName;
	}

	public void setPropertyName(String propertyName)
	{
		this.propertyName = propertyName;
	}

	@Override
	protected void applyRestriction(IClassDescriptor classDescriptor)
	{
		switch (getRestrictionType())
		{
			case VIEW:
				classDescriptor.
					getPropertyDescriptor(getPropertyName()).
					setHidden(true);
				break;
			case UPDATE:
				classDescriptor.
					getPropertyDescriptor(getPropertyName()).
					setReadOnly(true);
				break;
			case REMOVE:
				classDescriptor.
					getPropertyDescriptor(getPropertyName()).
					setReadOnly(true);
				break;
			default:
				break;
		}
	}


}
