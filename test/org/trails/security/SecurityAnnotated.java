package org.trails.security;

import org.trails.security.annotation.Restriction;
import org.trails.security.annotation.Security;

@Security(restrictions = { @Restriction(restrictionType = RestrictionType.UPDATE, requiredRole = "admin") })
public class SecurityAnnotated
{
    private String requiresAdmin;

    private String wideOpen;

    public SecurityAnnotated()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    @Security(restrictions = { @Restriction(restrictionType = RestrictionType.VIEW, requiredRole = "admin") })
    public String getRequiresAdmin()
    {
        return requiresAdmin;
    }

    public void setRequiresAdmin(String requiresAdmin)
    {
        this.requiresAdmin = requiresAdmin;
    }

}
