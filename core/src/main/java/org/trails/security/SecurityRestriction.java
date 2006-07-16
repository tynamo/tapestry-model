package org.trails.security;

import org.acegisecurity.GrantedAuthority;
import org.trails.descriptor.IClassDescriptor;

public abstract class SecurityRestriction
{

    public SecurityRestriction()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private String requiredRole;

    private RestrictionType restrictionType;
    
    public String getRequiredRole()
    {
        return requiredRole;
    }

    public void setRequiredRole(String requiredRole)
    {
        this.requiredRole = requiredRole;
    }

    public RestrictionType getRestrictionType()
    {
        return restrictionType;
    }

    public void setRestrictionType(RestrictionType restrictionType)
    {
        this.restrictionType = restrictionType;
    }

    protected boolean hasRequiredRole(GrantedAuthority[] autorities)
    {
       	for (int i=0; i < autorities.length ; i++) {
       		if (autorities[i].equals(getRequiredRole()))
       			return true;
       	}
       	return false;
        	/*
        	In SecurityContextHolder we don't have references to User, just to GrantedAuthorities.
        	We don't need any information stored under User, also...
        	
            List matches = (List) Ognl.getValue("authorities.{? authority == '"
                    + getRequiredRole() + "'}", user);
            return (matches.size() > 0);
        }
        catch (OgnlException oe)
        {
            oe.printStackTrace();
            return false;
        }
           */

    }

    protected abstract void applyRestriction(IClassDescriptor classDescriptor);
    
    public void restrict(GrantedAuthority[] autorities, IClassDescriptor classDescriptor)
    {
        if (!hasRequiredRole(autorities))
        {
            applyRestriction(classDescriptor);
        }
    
    }
}
