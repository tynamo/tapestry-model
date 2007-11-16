package org.trails.page;

import org.trails.descriptor.CollectionDescriptor;

public interface IAssociationPage extends IModelPage
{

	CollectionDescriptor getAssociationDescriptor();

	void setAssociationDescriptor(CollectionDescriptor associationDescriptor);

	Object getParent();

	void setParent(Object parent);
}
