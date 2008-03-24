package org.trails.engine;

import java.io.Serializable;

public class SqueezableTrailsPagesServiceParameter implements Serializable
{

	private String pageType;
	private String classDescriptor;
	private String model;
	private String assoc;
	private String parent;

	public SqueezableTrailsPagesServiceParameter(String pageType, String classDescriptor, String model, String assoc, String parent)
	{
		this.pageType = pageType;
		this.classDescriptor = classDescriptor;
		this.model = model;
		this.assoc = assoc;
		this.parent = parent;
	}

	public String getPageType()
	{
		return pageType;
	}

	public void setPageType(String pageType)
	{
		this.pageType = pageType;
	}

	public String getClassDescriptor()
	{
		return classDescriptor;
	}

	public void setClassDescriptor(String classDescriptor)
	{
		this.classDescriptor = classDescriptor;
	}

	public String getModel()
	{
		return model;
	}

	public void setModel(String model)
	{
		this.model = model;
	}

	public String getAssoc()
	{
		return assoc;
	}

	public void setAssoc(String assoc)
	{
		this.assoc = assoc;
	}

	public String getParent()
	{
		return parent;
	}

	public void setParent(String parent)
	{
		this.parent = parent;
	}
}
