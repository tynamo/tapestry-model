package org.trails.test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.trails.demo.Apple;
import org.trails.persistence.PersistenceService;

public class AppleTest extends AbstractTransactionalDataSourceSpringContextTests
{
	PersistenceService persistenceService;

	public void testApple() throws Exception
	{
		Apple apple = new Apple();
		apple.setName("Delicious");
		apple = persistenceService.save(apple);
	}

	@Override
	protected String[] getConfigLocations()
	{
		return new String[]{"applicationContext.xml"};
	}

	public PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}
}
