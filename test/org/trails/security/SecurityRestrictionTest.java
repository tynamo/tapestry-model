package org.trails.security;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.GrantedAuthorityImpl;

import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;
import org.trails.test.IBar;

import junit.framework.TestCase;

public class SecurityRestrictionTest extends TestCase
{
    
	public class TrailsAuthentication implements Authentication {

		private boolean authenticated;
		private GrantedAuthority[] authorities;
		private Object credentials;
		private Object details;
		private Object principal;
		private String name;
		
		public TrailsAuthentication(boolean authenticated, GrantedAuthority[] authority, String name) {
			this.authenticated = authenticated;
			this.authorities = authority;
			this.name = name;
		}
		
		public boolean isAuthenticated() {
			return authenticated;
		}
		public void setAuthenticated(boolean authenticated) {
			this.authenticated = authenticated;
		}
		public GrantedAuthority[] getAuthorities() {
			return authorities;
		}
		public void setAuthorities(GrantedAuthority[] authorities) {
			this.authorities = authorities;
		}
		public Object getCredentials() {
			return credentials;
		}
		public void setCredentials(Object credentials) {
			this.credentials = credentials;
		}
		public Object getDetails() {
			return details;
		}
		public void setDetails(Object details) {
			this.details = details;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Object getPrincipal() {
			return principal;
		}
		public void setPrincipal(Object principal) {
			this.principal = principal;
		}
		
		
	}
	
    protected IPropertyDescriptor propertyDescriptor;
    protected GrantedAuthority[] adminAuthority;
    protected GrantedAuthority[] noAdminAuthority;
    protected Authentication adminAuthentication;
    protected Authentication rootAuthentication;
    protected Authentication noAdminAuthentication;

    public SecurityRestrictionTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public SecurityRestrictionTest(String arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public void setUp()
    {
        propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, "bar", IBar.class);
        adminAuthority = new GrantedAuthority[] {new GrantedAuthorityImpl("admin")};
        noAdminAuthority = new GrantedAuthority[] {};
        rootAuthentication = new TrailsAuthentication(true, new GrantedAuthority[]{new GrantedAuthorityImpl("root")}, "root");
        adminAuthentication = new TrailsAuthentication(true, new GrantedAuthority[]{new GrantedAuthorityImpl("admin")}, "admin");
        noAdminAuthentication = new TrailsAuthentication(false, new GrantedAuthority[]{new GrantedAuthorityImpl("noAdmin")}, "noAdmin");
    }
    
    /**
     * Some test so All test cases can be executed.
     *
     */
    public void testFoo() 
    {
    	
    }

}
