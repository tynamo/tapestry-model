package org.trails.security;

import java.util.Date;
import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.persistence.HibernatePersistenceService;

public class ExpiringKeyAuthenticationProvider implements AuthenticationProvider {
	private static final Logger log = Logger.getLogger(ExpiringKeyAuthenticationProvider.class);
	
	private HibernatePersistenceService persistenceService;
	private UserDetailsService userDetailsService;

	public void setPersistenceService(HibernatePersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// This is called repetitively on the first request when the authentication is not yet established,
		// but a cookie is available. Only authenticate if not authenticated (no authorities found)
		if (authentication.getAuthorities() != null) return authentication;
		// Only process if principal is available
		if (authentication.getPrincipal() == null) return authentication;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExpiringKey.class);
		detachedCriteria.add(Restrictions.eq("name", authentication.getName()) );
		detachedCriteria.add(Restrictions.gt("expiresAfter", new Date() ) );
		
		List<ExpiringKey> expiringKeys = persistenceService.getInstances(ExpiringKey.class, detachedCriteria );
		if (expiringKeys.size() <= 0) throw new BadCredentialsException("No persistent credentials found");
		
		
		String providedToken = authentication.getCredentials().toString();
		if (providedToken == null) throw new BadCredentialsException("No remember me token provided");
		
		for (ExpiringKey key : expiringKeys) if (providedToken.equals(key.getValue()) ) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(key.getName());
			// TODO we should handle these specific exceptions here
			// A DisabledException must be thrown if an account is disabled and the AuthenticationManager can test for this state.
			// A LockedException must be thrown if an account is locked and the AuthenticationManager can test for account locking.
			if (userDetails == null) throw new BadCredentialsException("Token found, but user doesn't exist");
	    log.info("Successfully authenticated user " + authentication.getName() + " using expiring key");
			return new UserKeyAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), userDetails.getAuthorities() );
		}
		throw new BadCredentialsException("No matching token available");
	}

	public boolean supports(Class authenticationClass) {
		return (UserKeyAuthenticationToken.class.isAssignableFrom(authenticationClass));
	}
}
