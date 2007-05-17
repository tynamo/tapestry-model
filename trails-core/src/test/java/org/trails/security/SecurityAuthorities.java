/*
 * Created on 20/12/2005 by Eduardo Piva - <eduardo@gwe.com.br>
 *
 */
package org.trails.security;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;

public class SecurityAuthorities
{

	public class TrailsAuthentication implements Authentication
	{

		private boolean authenticated;
		private GrantedAuthority[] authorities;
		private Object credentials;
		private Object details;
		private Object principal;
		private String name;

		public TrailsAuthentication(boolean authenticated, GrantedAuthority[] authority, String name)
		{
			this.authenticated = authenticated;
			this.authorities = authority;
			this.name = name;
		}

		public boolean isAuthenticated()
		{
			return authenticated;
		}

		public void setAuthenticated(boolean authenticated)
		{
			this.authenticated = authenticated;
		}

		public GrantedAuthority[] getAuthorities()
		{
			return authorities;
		}

		public void setAuthorities(GrantedAuthority[] authorities)
		{
			this.authorities = authorities;
		}

		public Object getCredentials()
		{
			return credentials;
		}

		public void setCredentials(Object credentials)
		{
			this.credentials = credentials;
		}

		public Object getDetails()
		{
			return details;
		}

		public void setDetails(Object details)
		{
			this.details = details;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public Object getPrincipal()
		{
			return principal;
		}

		public void setPrincipal(Object principal)
		{
			this.principal = principal;
		}


	}

	public GrantedAuthority[] adminAuthority;
	public GrantedAuthority[] noAdminAuthority;
	public Authentication adminAuthentication;
	public Authentication rootAuthentication;
	public Authentication noAdminAuthentication;

	public SecurityAuthorities()
	{
		adminAuthority = new GrantedAuthority[]{new GrantedAuthorityImpl("admin")};
		noAdminAuthority = new GrantedAuthority[]{};
		rootAuthentication = new TrailsAuthentication(true, new GrantedAuthority[]{new GrantedAuthorityImpl("root")}, "root");
		adminAuthentication = new TrailsAuthentication(true, new GrantedAuthority[]{new GrantedAuthorityImpl("admin")}, "admin");
		noAdminAuthentication = new TrailsAuthentication(false, new GrantedAuthority[]{new GrantedAuthorityImpl("noAdmin")}, "noAdmin");
	}

}
