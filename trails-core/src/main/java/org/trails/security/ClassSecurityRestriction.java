package org.trails.security;


import org.trails.descriptor.IClassDescriptor;

public class ClassSecurityRestriction extends SecurityRestriction
{
	protected void applyRestriction(IClassDescriptor classDescriptor)
	{
		switch (getRestrictionType())
		{
			case VIEW:
				classDescriptor.setHidden(true);
				break;
			case UPDATE:
				classDescriptor.setAllowSave(false);
				break;
			case REMOVE:
				classDescriptor.setAllowRemove(false);
				break;
			default:
				break;
		}
	}

}
