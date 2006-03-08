/*
 * Created on 10/01/2006
 *
 */
package org.trails.security;

import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.trails.persistence.PersistenceService;
import org.trails.security.domain.Role;
import org.trails.security.domain.User;

/**
 * This class startup the db for security.
 * @author Eduardo Fernandes Piva (eduardo@gwe.com.br)
 *
 */
public class SecurityStartup {

	private PersistenceService persistenceService;
	private List<User> defaultUsers;
	private List<Role> defaultRoles;

	public void startup() {
		printInfo();
		
		for (Role role : defaultRoles) {
			persistenceService.save(role);
		}
		for (User user : defaultUsers) {
			LinkedHashSet<Role> set = new LinkedHashSet<Role>();
			if (user.getRoles() != null) {
				for (Role role : user.getRoles()) {
					List roles = (List) persistenceService.getInstances(role);
					if (roles != null && roles.size() > 0) {
						set.add((Role) roles.get(0));						
					}
				}				
			}
			user.setRoles(set);
			persistenceService.save(user);
		}
	}
	
	private void printInfo() {
		System.out.println("Adding users:");
		for (User user : defaultUsers) {
			System.out.println(user + " with roles " + user.getRoles() + " size = " + user.getRoles().size());
		}
		
	}

	/**
	 * This can be called from command line or directly from ant.
	 * @param args
	 */
	public static void main(String args[]) {
		
		if (args.length != 1)
			throw new RuntimeException("location of applicationContext.xml must be specified");
		
		ApplicationContext context = new FileSystemXmlApplicationContext(args[0]);
		SecurityStartup bootStrap = (SecurityStartup) context.getBean("securityStartup");
		bootStrap.startup();
	}


	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	
	public void setDefaultRoles(List<Role> defaultRoles) {
		this.defaultRoles = defaultRoles;
	}


	public void setDefaultUsers(List<User> defaultUsers) {
		this.defaultUsers = defaultUsers;
	}

}
