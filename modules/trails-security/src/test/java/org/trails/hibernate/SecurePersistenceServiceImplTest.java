package org.trails.hibernate;

import java.util.List;

import junit.framework.TestCase;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.hibernate.type.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.descriptor.DescriptorService;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.security.OwnerRequired;
import org.trails.security.TrailsUserDAO;
import org.trails.security.domain.Role;
import org.trails.security.domain.User;
import org.trails.seeddata.SeedDataInitializer;
import org.trails.seeddata.SpringSeedEntityInitializer;

public class SecurePersistenceServiceImplTest extends TestCase {
	private ApplicationContext applicationContext;
	private SpringSeedEntityInitializer seedDataInitializer;
	private HibernatePersistenceService persistenceService;
	private DescriptorService descriptorService;

	@Override
	protected void setUp() throws Exception
	{
		applicationContext = new ClassPathXmlApplicationContext(new String[]{"applicationContext-test.xml", "seed-data-test.xml"});
		persistenceService = (HibernatePersistenceService) applicationContext.getBean("persistenceService");
		seedDataInitializer = (SpringSeedEntityInitializer) applicationContext.getBean(SeedDataInitializer.class.getSimpleName());
		seedDataInitializer.init();
	}
	
	public void testSelfAssociationRestriction() {
  	SecurityContext securityContext = new SecurityContextImpl();
  	GrantedAuthority[] authorities = new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_USER")};
  	Authentication authentication = new TestingAuthenticationToken("username1", "password", authorities);
  	securityContext.setAuthentication(authentication);
  	SecurityContextHolder.setContext(securityContext);
  	
  	User user = new User();
  	user.setUsername("username1");
  	user.setFirstName("Test");
  	user.setLastName("User1");
  	user.setPassword("password");
  	user.setConfirmPassword("password");
  	persistenceService.save(user);
  	
    List<User> users = persistenceService.getAllInstances(User.class);
  	assertEquals(1,users.size());
    
  	authentication = new TestingAuthenticationToken("nonExistentUsername", "password", authorities);
  	securityContext.setAuthentication(authentication);
  	SecurityContextHolder.setContext(securityContext);
    users = persistenceService.getAllInstances(User.class);
  	assertEquals(0,users.size());
	}

	public void tearDown() {
		// Clear context so not to interfere with other tests
  	SecurityContextHolder.clearContext();
	}
}
