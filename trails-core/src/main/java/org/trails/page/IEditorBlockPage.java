package org.trails.page;

import org.trails.descriptor.IPropertyDescriptor;


public interface IEditorBlockPage extends IModelPage
{

	IPropertyDescriptor getDescriptor();

	void setDescriptor(IPropertyDescriptor Descriptor);

}
