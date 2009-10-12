package org.tynamo.security;

import org.acegisecurity.GrantedAuthority;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tynamo.descriptor.IClassDescriptor;

public abstract class SecurityRestriction
{

	private static final Log LOG = LogFactory.getLog(SecurityRestriction.class);

	public SecurityRestriction()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	private String requiredRole[];

	private RestrictionType restrictionType;

	public String[] getRequiredRole()
	{
		return requiredRole;
	}

	public void setRequiredRole(String[] requiredRole)
	{
		if (requiredRole == null) this.requiredRole = new String[]{};
		else this.requiredRole = requiredRole;
	}

	public RestrictionType getRestrictionType()
	{
		return restrictionType;
	}

	public void setRestrictionType(RestrictionType restrictionType)
	{
		this.restrictionType = restrictionType;
	}

	protected boolean hasRequiredRole(GrantedAuthority[] authorities)
	{
		for (GrantedAuthority authority : authorities)
			for (String role : requiredRole) if (role.equals(authority.getAuthority()) ) return true;
		return false;
	}

	protected abstract void applyRestriction(IClassDescriptor classDescriptor);

	public void restrict(GrantedAuthority[] authorities, IClassDescriptor classDescriptor)
	{
		if (!hasRequiredRole(authorities))
		{
			applyRestriction(classDescriptor);
		}

	}
}
