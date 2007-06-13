package org.trails.security;

import org.trails.security.annotation.UpdateRequiresRole;
import org.trails.security.annotation.ViewRequiresRole;

@UpdateRequiresRole("admin")
public class SecurityAnnotated
{
	private String requiresAdmin;

	public SecurityAnnotated()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@ViewRequiresRole("admin")
	public String getRequiresAdmin()
	{
		return requiresAdmin;
	}

	public void setRequiresAdmin(String requiresAdmin)
	{
		this.requiresAdmin = requiresAdmin;
	}

}
