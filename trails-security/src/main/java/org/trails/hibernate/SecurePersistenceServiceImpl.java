package org.trails.hibernate;

import java.io.Serializable;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.trails.security.EntitySecurityException;
import org.trails.security.annotation.ViewRequiresAssociation;
import org.trails.security.annotation.ViewRequiresRole;

public class SecurePersistenceServiceImpl extends HibernatePersistenceServiceImpl {
	@Override
	protected DetachedCriteria alterCriteria(Class type, DetachedCriteria criteria) {
		SecurityContext context = SecurityContextHolder.getContext();
		// Assume that context should have been established for each request, and if it's not,
		// it's an internal service call 
		if (context == null || context.getAuthentication() == null) return criteria;

		// Check first if user has permission granted by a role 
		ViewRequiresRole viewRoleRestriction = (ViewRequiresRole)type.getAnnotation(ViewRequiresRole.class );
		if (viewRoleRestriction != null) for (GrantedAuthority authority : context.getAuthentication().getAuthorities())
			for (String role : viewRoleRestriction.value()) if (authority.getAuthority().equals(role) ) return criteria;

		ViewRequiresAssociation viewRestriction = (ViewRequiresAssociation)type.getAnnotation(ViewRequiresAssociation.class );
		
		if (viewRoleRestriction == null && viewRestriction == null) return criteria;
		
		if (viewRestriction == null) {
			// At this point we know the user should have no access to the entities, because there's
			// a role restriction but user doesn't have a suitable role and there's no association
			throw new EntitySecurityException(null, "No suitable role or association");
		}
		
		String currentUsername = context.getAuthentication().getName();
		String ownerPropertyAssociation = viewRestriction.value();
		// username as in Acegi UserDetails
		SimpleExpression usernameRestriction = Restrictions.eq("username",currentUsername);
		if ("".equals(ownerPropertyAssociation)) criteria.add(usernameRestriction);
		else criteria.createCriteria(ownerPropertyAssociation).add(usernameRestriction);			
		return criteria;
	}

	public <T> T loadInstance(final Class<T> type, Serializable id)
	{
		return getInstance(type, id);
	}
	
}
