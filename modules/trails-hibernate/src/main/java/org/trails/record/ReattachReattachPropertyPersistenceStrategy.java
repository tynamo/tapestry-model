package org.trails.record;

import org.trails.persistence.HibernatePersistenceService;

public class ReattachReattachPropertyPersistenceStrategy extends ReattachPropertyPersistenceStrategy
{
	HibernatePersistenceService persistenceService;

	protected String getStrategyId()
	{
		return "reattach-reattach";
	}

	protected Object reattach(Object entity)
	{
		persistenceService.reattach(entity);
		return entity;
	}

	public void setPersistenceService(HibernatePersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}
}
