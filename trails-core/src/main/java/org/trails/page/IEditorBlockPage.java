package org.trails.page;

import org.apache.tapestry.IPage;
import org.trails.descriptor.IPropertyDescriptor;


public interface IEditorBlockPage extends IPage, IModelPage
{

	IPropertyDescriptor getDescriptor();

	void setDescriptor(IPropertyDescriptor Descriptor);

}
