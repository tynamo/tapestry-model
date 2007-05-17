package org.trails.component;

import org.apache.tapestry.BaseComponent;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.hibernate.EmbeddedDescriptor;

public abstract class EmbeddedEditor extends BaseComponent
{

	public EmbeddedEditor()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public abstract IPropertyDescriptor getDescriptor();

	public abstract void setDescriptor(IPropertyDescriptor descriptor);

	public abstract Object getModel();

	public abstract void setModel(Object model);

	public EmbeddedDescriptor getEmbeddedDescriptor()
	{
		return (EmbeddedDescriptor) getDescriptor();
	}
}
