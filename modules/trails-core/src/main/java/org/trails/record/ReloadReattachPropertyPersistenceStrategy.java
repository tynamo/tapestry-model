package org.trails.record;

import org.trails.persistence.PersistenceService;
import org.trails.util.Utils;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;


public class ReloadReattachPropertyPersistenceStrategy extends ReattachPropertyPersistenceStrategy
{

	DescriptorService descriptorService;
	PersistenceService persistenceService;

	protected String getStrategyId()
	{
		return "reattach-reload";
	}

	protected Object reattach(Object entity)
	{
		IClassDescriptor classDescriptor = descriptorService.getClassDescriptor(Utils.checkForCGLIB(entity.getClass()));
		return persistenceService
				.loadInstance(classDescriptor.getType(), persistenceService.getIdentifier(entity, classDescriptor));
	}

	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public void setDescriptorService(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}
}