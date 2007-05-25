package org.trails.component;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.ComponentClass;
import org.trails.descriptor.IIdentifierDescriptor;
import org.trails.hibernate.HasAssignedIdentifier;

@ComponentClass(allowBody = true, allowInformalParameters = false)
public abstract class HibernateIdentifier extends Identifier
{

	@Asset(value = "/org/trails/component/Identifier.html")
	public abstract IAsset get$template();

	public boolean isEditable()
	{
		return !((IIdentifierDescriptor) getDescriptor()).isGenerated() && !((HasAssignedIdentifier) getModel()).isSaved();
	}
}
