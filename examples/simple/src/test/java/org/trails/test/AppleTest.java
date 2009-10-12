package org.tynamo.test;

import org.tynamo.examples.simple.entities.Apple;
import org.tynamo.services.PersistenceService;

public class AppleTest //extends AbstractTransactionalDataSourceSpringContextTests
{
	PersistenceService persistenceService;

	public void testApple() throws Exception
	{
		Apple apple = new Apple();
		apple.setName("Delicious");
		apple = persistenceService.save(apple);
	}

	//	@Override
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
