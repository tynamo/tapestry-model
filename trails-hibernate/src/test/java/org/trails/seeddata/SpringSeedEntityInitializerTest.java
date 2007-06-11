package org.trails.seeddata;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.persistence.HibernatePersistenceService;

public class SpringSeedEntityInitializerTest extends TestCase
{
	private ApplicationContext applicationContext;
	private SpringSeedEntityInitializer seedDataInitializer;
	private HibernatePersistenceService persistenceService;

	@Override
	protected void setUp() throws Exception
	{
		applicationContext = new ClassPathXmlApplicationContext(new String[]{"applicationContext-test.xml", "seed-data-test.xml"});
		persistenceService = (HibernatePersistenceService) applicationContext.getBean("persistenceService");
		seedDataInitializer = (SpringSeedEntityInitializer) applicationContext.getBean(SeedDataInitializer.class.getSimpleName());
	}
	
	public void testInit() {
		seedDataInitializer.init();
	}
}
