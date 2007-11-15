package org.trails.record;

import org.trails.persistence.PersistenceService;


public class ReloadReattachPropertyPersistenceStrategy extends ReattachPropertyPersistenceStrategy
{

	PersistenceService persistenceService;

	protected String getStrategyId()
	{
		return "reattach-reload";
	}

	protected Object reattach(Object entity)
	{
		return null; //persistenceService.reloadInstance(entity);
	}

	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}
}