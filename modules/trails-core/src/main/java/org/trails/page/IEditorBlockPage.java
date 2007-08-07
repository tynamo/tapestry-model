package org.trails.page;

import org.apache.tapestry.IPage;
import org.trails.descriptor.IPropertyDescriptor;


public interface IEditorBlockPage extends IPage
{

	Object getModel();

	void setModel(Object model);

	IPropertyDescriptor getDescriptor();

	void setDescriptor(IPropertyDescriptor Descriptor);

	String getEditPageName();

	void setEditPageName(String EditPageName);
}
