package org.trails.component.blob;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.extension.BlobDescriptorExtension;

@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class TrailsDownload extends BaseComponent
{
	@InjectObject("service:trails.core.FilePersister")
	public abstract IFilePersister getFilePersister();

	@InjectObject("service:trails.core.IconResolver")
	public abstract IconResolverImpl getIconResolver();

	@Parameter(required = true)
	public abstract Object getModel();

	public abstract void setModel(Object bytes);

	@Parameter(required = true)
	public abstract IPropertyDescriptor getPropertyDescriptor();

	public abstract void setPropertyDescriptor(IPropertyDescriptor descriptor);

	@Parameter(required = false, defaultValue = "page.classDescriptor")
	public abstract IClassDescriptor getClassDescriptor();

	public abstract void setClassDescriptor(IClassDescriptor ClassDescriptor);

	public BlobDescriptorExtension.RenderType getRenderType()
	{
		return getPropertyDescriptor().getExtension(BlobDescriptorExtension.class).getRenderType();
	}

	public IAsset getByteArrayAsset()
	{
		return getFilePersister().getAsset(getClassDescriptor(), getPropertyDescriptor(), getModel());
	}

	public IAsset getIcon()
	{
		return getIconResolver().getAsset(getContentType());
	}

	private String getContentType()
	{
		return getFilePersister().getContentType(getClassDescriptor(), getPropertyDescriptor(), getModel());
	}

	public String getFileName()
	{
		return getFilePersister().getFileName(getClassDescriptor(), getPropertyDescriptor(), getModel());
	}

}
