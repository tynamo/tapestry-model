package org.trails.callback;

import org.trails.descriptor.CollectionDescriptor;
import org.trails.persistence.PersistenceService;

public class EditCollectionMemberCallback extends CollectionCallback
{

	public EditCollectionMemberCallback(String pageName, Object model, CollectionDescriptor collectionDescriptor)
	{
		super(pageName, model, collectionDescriptor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void save(PersistenceService persistenceService, Object newObject)
	{
		setModel(persistenceService.reload(getModel()));
	}
}
