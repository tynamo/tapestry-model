package org.trails.security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;

/* This class really isn't needed as we could just use UsernamePasswordAuthenticationToken, 
 * but I couldn't find any way to limit the AuthenticationProvider to process just these 
 * tokens unless I subclasseed AuthenticationToken
 */
public class UserKeyAuthenticationToken extends UsernamePasswordAuthenticationToken {
	public UserKeyAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}
	public UserKeyAuthenticationToken(Object principal, Object credentials, GrantedAuthority[] authorities) {
		super(principal, credentials, authorities);
	}
}
