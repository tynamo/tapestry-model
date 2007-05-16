/*
 * Created on 10/01/2006
 *
 */
package org.trails.security;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.persistence.PersistenceService;
import org.trails.security.domain.Role;
import org.trails.security.domain.User;
import org.trails.seeddata.SeedDataInitializer;

/**
 * This class startup the db for security.
 *
 * @author Eduardo Fernandes Piva (eduardo@gwe.com.br)
 */
@Deprecated
public class SecurityStartup
{

	private PersistenceService persistenceService;
	private List<User> defaultUsers;
	private List<Role> defaultRoles;
	private SeedDataInitializer seedDataInitializer;

	@Deprecated
	public void startup()
	{
		seedDataInitializer.init();
	}

	/**
	 * This can be called from command line or directly from ant.
	 *
	 * @param args
	 */
	public static void main(String args[])
	{
		String appContextFile = "applicationContext.xml";
		if (args.length == 1)
		{
			appContextFile = args[0];
		}

		ApplicationContext context = new ClassPathXmlApplicationContext(appContextFile);
		SecurityStartup bootStrap = (SecurityStartup) context.getBean("securityStartup");
		bootStrap.startup();
	}


	@Deprecated
	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}


	@Deprecated
	public void setDefaultRoles(List<Role> defaultRoles)
	{
		this.defaultRoles = defaultRoles;
	}

	@Deprecated
	public void setDefaultUsers(List<User> defaultUsers)
	{
		this.defaultUsers = defaultUsers;
	}

	public void setSeedDataInitializer(SeedDataInitializer seedDataInitializer)
	{
		this.seedDataInitializer = seedDataInitializer;
	}

}
