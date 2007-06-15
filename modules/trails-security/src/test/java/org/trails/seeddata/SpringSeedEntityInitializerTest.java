package org.trails.seeddata;

import org.acegisecurity.userdetails.UserDetails;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.security.TrailsUserDAO;
import org.trails.security.domain.Role;
import org.trails.security.domain.User;
import org.trails.test.Bar;
import org.trails.test.Foo;
import org.trails.test.MockableTransactionalTestCase;

public class SpringSeedEntityInitializerTest extends MockableTransactionalTestCase
{
	private TrailsUserDAO userDAO;
	private Role roleUser;
	private Role roleAdmin;
	private HibernatePersistenceService persistenceService;

	@Override
	public void onSetUpBeforeTransaction() throws Exception
	{
		super.onSetUpBeforeTransaction();
		userDAO = (TrailsUserDAO) applicationContext.getBean("trailsUserDAO");
		roleUser = (Role) applicationContext.getBean("roleUser");
		roleAdmin = (Role) applicationContext.getBean("roleAdmin");
		this.persistenceService = (HibernatePersistenceService)super.persistenceService;
	}

	public void testInit()
	{
		UserDetails user = userDAO.loadUserByUsername("user");
		assertEquals(user.getAuthorities()[0], roleUser);
	}

	public void testArbitraryEntitySeeded()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
		criteria.add(Restrictions.eq("name", "seed foo"));
		Foo foo = (Foo) persistenceService.getInstance(Foo.class, criteria);
		assertNotNull(foo);
	}

	public void testSeedingEntityWithoutUniquelyIdentifyingProperty()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Bar.class);
		criteria.add(Restrictions.eq("name", "based on example"));
		try
		{
			Object object = persistenceService.getInstance(Bar.class, criteria);
			if (object == null) fail("Seed entity not found");
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			fail("More than one entity returned");
		}
	}

	public void testEntityAlreadySeeded()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("username", "admin"));
		User user = (User) persistenceService.getInstance(User.class, criteria);
		user.setLastName("Changed something");
		persistenceService.save(user);
		// Data is not re-seeded, so it shouldn't get overwritten
		SpringSeedEntityInitializer seedDataInitializer = (SpringSeedEntityInitializer) applicationContext.getBean(SeedDataInitializer.class.getSimpleName());
		seedDataInitializer.init();
		user = (User) persistenceService.getInstance(User.class, criteria);
		assertEquals("Changed something", user.getLastName());
	}

	/*
	public void tearDown()
	{
		// Clean up
		// Seems I have to clean up because HSQL must be using static class members
		UserDetails user = userDAO.loadUserByUsername("user");
		persistenceService.remove(user);
		user = userDAO.loadUserByUsername("admin");
		persistenceService.remove(user);
		persistenceService.remove(roleUser);
		persistenceService.remove(roleAdmin);

		Foo foo = new Foo();
		foo.setId(1);
		persistenceService.remove(foo);
		// TODO see if you can do this instead: sessionFactory.dropDatabaseSchema();
	}
	*/

}
