package org.trails.engine;

import java.io.Serializable;

import org.apache.hivemind.util.Defense;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.page.PageType;


public class TrailsPagesServiceParameter implements Serializable
{

	private PageType pageType;
	private IClassDescriptor classDescriptor;
	private Object model;
	private CollectionDescriptor associationDescriptor;
	private Object parent;

	public TrailsPagesServiceParameter(PageType pageType, IClassDescriptor classDescriptor)
	{
		Defense.notNull(pageType, "pageName");
		Defense.notNull(classDescriptor, "classDescriptor");
		this.pageType = pageType;
		this.classDescriptor = classDescriptor;
	}

	public TrailsPagesServiceParameter(PageType pageType, IClassDescriptor classDescriptor, Object model)
	{
		this(pageType, classDescriptor);
		this.model = model;
	}

	public TrailsPagesServiceParameter(PageType pageType, IClassDescriptor classDescriptor, Object model, CollectionDescriptor associationDescriptor, Object parent)
	{
		this(pageType, classDescriptor, model);
		this.associationDescriptor = associationDescriptor;
		this.parent = parent;
	}

	public PageType getPageType()
	{
		return pageType;
	}

	public IClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}

	public Object getModel()
	{
		return model;
	}

	public CollectionDescriptor getAssociationDescriptor()
	{
		return associationDescriptor;
	}

	public Object getParent()
	{
		return parent;
	}
}
