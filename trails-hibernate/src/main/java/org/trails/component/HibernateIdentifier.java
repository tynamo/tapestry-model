package org.trails.component;

import org.apache.tapestry.annotations.ComponentClass;
import org.trails.descriptor.IIdentifierDescriptor;
import org.trails.hibernate.HasAssignedIdentifier;

@ComponentClass(allowBody = false, allowInformalParameters = false)
public abstract class HibernateIdentifier extends Identifier
{

	public boolean isEditable()
	{
		return !((IIdentifierDescriptor) getDescriptor()).isGenerated() && !((HasAssignedIdentifier) getModel()).isSaved();
	}
}
