/*
 * Created on 10/01/2006
 *
 */
package org.trails.security;


import java.util.LinkedHashSet;
import java.util.List;

import junit.framework.TestCase;
import ognl.Ognl;
import ognl.OgnlException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.trails.persistence.PersistenceService;
import org.trails.security.domain.Role;
import org.trails.security.domain.User;

/**
 * 
 * @author Eduardo Fernandes Piva (eduardo@gwe.com.br)
 *
 */
public class SecurityStartupTest extends TestCase {
	
	private ApplicationContext appContext;
	private SecurityStartup bootStrap;
	private PersistenceService persistenceService;
	private PlatformTransactionManager txManager;
	
	private Role roleAdmin;
	private Role roleUser;
	private User user;
	private User admin;	

	
	@Override
	protected void setUp() throws Exception {
        appContext = new ClassPathXmlApplicationContext(new String[]{
        "applicationContext-test.xml", "seed-data-test.xml"} );
        bootStrap = (SecurityStartup) appContext.getBean("securityStartup");
        persistenceService = (PersistenceService) appContext.getBean("persistenceService");
        txManager = (PlatformTransactionManager) appContext.getBean("transactionManager");

        roleAdmin = new Role();
        roleUser = new Role();
        user = new User();
        admin = new User();
        
        roleAdmin.setName("ROLE_MANAGER");
        roleAdmin.setDescription("Admin role");
        
        roleUser.setName("ROLE_USER");
        roleUser.setDescription("User role");
        
        user.setUsername("user");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setPassword("user");
        user.setConfirmPassword("user");
        
        admin.setUsername("admin");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword("admin");
        admin.setConfirmPassword("admin");
        
        LinkedHashSet<Role> userRoles = new LinkedHashSet<Role>();
        LinkedHashSet<Role> adminRoles = new LinkedHashSet<Role>();
        
        userRoles.add(roleUser);
        adminRoles.add(roleUser);
        adminRoles.add(roleAdmin);
        
        user.setRoles(userRoles);
        admin.setRoles(adminRoles);
	}
	
	public void testStartup() throws Exception {
		TransactionTemplate tt = new TransactionTemplate(txManager);
		
		tt.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				List roles = persistenceService.getAllInstances(Role.class);
				List users = persistenceService.getAllInstances(User.class);

				assertEquals(roles.size(), 0);
				assertEquals(users.size(), 0);

				bootStrap.startup();
				roles = persistenceService.getAllInstances(Role.class);
				users = persistenceService.getAllInstances(User.class);
				
				assertEquals(roles.size(), 2);
				assertEquals(users.size(), 2);
				
				try
				{
					User adminUser = (User)Ognl.getValue("#root.{? username == 'admin'}[0]", users);
					assertEquals(2, adminUser.getRoles().size());
				}
				catch (OgnlException oe)
                {
                    //oe.printStackTrace();
                }
				assertTrue(((Role)roles.get(0)).getName().equals(roleUser.getName())
						|| ((Role)roles.get(1)).getName().equals(roleUser.getName()));
				assertTrue(((Role)roles.get(0)).getName().equals(roleAdmin.getName())
						|| ((Role)roles.get(1)).getName().equals(roleAdmin.getName()));
				assertTrue(((User)users.get(0)).getUsername().equals(user.getUsername())
						|| ((User)users.get(1)).getUsername().equals(user.getUsername()));
				assertTrue(((User)users.get(0)).getUsername().equals(admin.getUsername())
						|| ((User)users.get(1)).getUsername().equals(admin.getUsername()));
				
				status.setRollbackOnly(); // force rollback so we don't really change things in db.
			}
			
		});		
		
	}
	
}
