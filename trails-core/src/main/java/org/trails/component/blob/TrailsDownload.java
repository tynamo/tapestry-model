package org.trails.component.blob;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.asset.AssetFactory;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.extension.BlobDescriptorExtension;

@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class TrailsDownload extends BaseComponent
{
	private final static String noIcon = "/images/cross.png";
	private final static String noImage = "/images/noimage.jpg";

	@InjectObject("service:trails.core.FilePersister")
	public abstract IFilePersister getFilePersister();

	@InjectObject("service:trails.core.IconResolver")
	public abstract IconResolver getIconResolver();

	@InjectObject("service:tapestry.asset.ContextAssetFactory")
	public abstract AssetFactory getContextAssetFactory();

	@Parameter(required = true)
	public abstract Object getModel();

	@Parameter(required = true)
	public abstract IPropertyDescriptor getPropertyDescriptor();

	@Parameter(required = false, defaultValue = "page.classDescriptor")
	public abstract IClassDescriptor getClassDescriptor();

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
		return getContentType() != null ? getIconResolver().getAsset(getContentType()) : getNoIcon();
	}

	private String getContentType()
	{
		return getFilePersister().getContentType(getClassDescriptor(), getPropertyDescriptor(), getModel());
	}

	public String getFileName()
	{
		return getFilePersister().getFileName(getClassDescriptor(), getPropertyDescriptor(), getModel());
	}

	public IAsset getNoIcon()
	{
		return getContextAssetFactory().createAbsoluteAsset(noIcon, null, null);
	}

	public IAsset getNoImage()
	{
		return getContextAssetFactory().createAbsoluteAsset(noImage, null, null);
	}
}
