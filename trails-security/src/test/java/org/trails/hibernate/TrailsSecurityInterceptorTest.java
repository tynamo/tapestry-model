/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.hibernate;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.hibernate.type.Type;
import org.jmock.MockObjectTestCase;
import org.trails.security.OwnerRequired;
import org.trails.security.TrailsSecurityException;
import org.trails.security.domain.Role;
import org.trails.security.domain.User;
import org.trails.security.test.FooSecured;
import org.trails.test.Foo;

public class TrailsSecurityInterceptorTest extends MockObjectTestCase {
	private TrailsSecurityInterceptor interceptor = null; 
	public void setUp() {
		interceptor = new TrailsSecurityInterceptor();
	}
	
  public void testSelfAssociationRestrictionOnUpdate() {
  	SecurityContext securityContext = new SecurityContextImpl();
  	GrantedAuthority[] authorities = new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_USER")};
  	Authentication authentication = new TestingAuthenticationToken("username", "password", authorities);
  	securityContext.setAuthentication(authentication);
  	SecurityContextHolder.setContext(securityContext);
  	
    User user = new User();
    user.setUsername("username");

  	interceptor.onFlushDirty(user, 1, new Object[]{}, new Object[]{}, new String[]{}, new Type[]{});
  	
  	user.setUsername("wrongUsername");
  	try {
  		interceptor.onFlushDirty(user, 1, new Object[]{}, new Object[]{}, new String[]{}, new Type[]{});
    	fail("OwnerRequired exception should have been thrown on wrong user name");
  	}
  	catch(OwnerRequired e) {
  		// OK
  	}
  }
  
  public void testOwnershipRestrictionOnInsert() {
  	SecurityContext securityContext = new SecurityContextImpl();
  	GrantedAuthority[] authorities = new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_USER")};
  	Authentication authentication = new TestingAuthenticationToken("username", "password", authorities);
  	securityContext.setAuthentication(authentication);
  	SecurityContextHolder.setContext(securityContext);
  	FooSecured secureFoo = new FooSecured();
    User user = new User();
    user.setUsername("username");
    secureFoo.setOwner(user);
  	
  	interceptor.onSave(secureFoo, 1, new Object[]{}, new String[]{}, new Type[]{});
  	
    user.setUsername("differentName");
  	try {
  		interceptor.onSave(secureFoo, 1, new Object[]{}, new String[]{}, new Type[]{});
    	fail("OwnerRequired exception should have been thrown on non-owner");
  	}
  	catch(OwnerRequired e) {
  		// OK
  	}
  	
		Foo foo = new Foo();
  	try {
  		interceptor.onSave(foo, 1, new Object[]{}, new String[]{}, new Type[]{});
  		fail("Exception should have been thrown because association is invalid");
  	}
  	catch(TrailsSecurityException e) {
  		// OK
  	}
  }
  
  public void testRoleRestrictionOnRemove() {
  	SecurityContext securityContext = new SecurityContextImpl();
  	GrantedAuthority[] authorities = new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_MANAGER")};
  	Authentication authentication = new TestingAuthenticationToken("manager", "password", authorities);
  	securityContext.setAuthentication(authentication);
  	SecurityContextHolder.setContext(securityContext);
  	Role role = new Role();
  	
  	interceptor.onLoad(role, 1, new Object[]{}, new String[]{}, new Type[]{});
  }
  
	public void tearDown() {
		// Not to interfere with other tests
		SecurityContextHolder.clearContext();		
	}
}
