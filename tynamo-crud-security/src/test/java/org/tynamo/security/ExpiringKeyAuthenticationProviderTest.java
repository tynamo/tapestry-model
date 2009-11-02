package org.tynamo.security;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetailsService;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.tynamo.persistence.HibernatePersistenceService;

public class ExpiringKeyAuthenticationProviderTest extends MockObjectTestCase {
	private ExpiringKeyAuthenticationProvider rememberMeAuthenticationProvider;
	private Mock persistenceServiceMock;
	private Mock userDetailsServiceMock;
	
	
	public void setUp() {
		rememberMeAuthenticationProvider = new ExpiringKeyAuthenticationProvider();
		persistenceServiceMock = mock(HibernatePersistenceService.class );
		userDetailsServiceMock = mock(UserDetailsService.class );
		rememberMeAuthenticationProvider.setPersistenceService((HibernatePersistenceService)persistenceServiceMock.proxy());
		rememberMeAuthenticationProvider.setUserDetailsService((UserDetailsService)userDetailsServiceMock.proxy());
	}
	
	public void testAuthenticate() {
		Authentication authentication = new UserKeyAuthenticationToken("username", "111");
		List<ExpiringKey> credentials = new ArrayList<ExpiringKey>();
		credentials.add(new ExpiringKey("username", (BigInteger.valueOf(111)).toString(), new Date((new Date()).getTime() + 3600 * 1000) ) );
		
		persistenceServiceMock.expects(once()).method("getInstances").will(returnValue(credentials));
		userDetailsServiceMock.expects(once()).method("loadUserByUsername").will(returnValue(new User("username", "111", true, true, true, true, new GrantedAuthority[]{})));
		
		rememberMeAuthenticationProvider.authenticate(authentication);
		
		
	}

}
