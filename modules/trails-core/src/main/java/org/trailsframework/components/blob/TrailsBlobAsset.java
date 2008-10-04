package org.trails.component.blob;

import org.apache.tapestry.asset.AbstractAsset;
import org.apache.tapestry.engine.ILink;
import org.trails.descriptor.IPropertyDescriptor;

import java.io.InputStream;
import java.io.Serializable;

/**
 * An implementation of {@link org.apache.tapestry.IAsset} for assets that are entity properties.
 */
public class TrailsBlobAsset extends AbstractAsset
{

	private BlobDownloadService bytesService;

	private Serializable id;

	private IPropertyDescriptor propertyDescriptor;

	public TrailsBlobAsset(BlobDownloadService chartService, IPropertyDescriptor propertyDescriptor, Serializable id)
	{
		super(null, null);
		this.bytesService = chartService;
		this.id = id;
		this.propertyDescriptor = propertyDescriptor;
	}

	public IPropertyDescriptor getPropertyDescriptor()
	{
		return propertyDescriptor;
	}

	public Serializable getId()
	{
		return id;
	}

	public String buildURL()
	{
		ILink l = bytesService.getLink(false, new Object[]{this});
		return l.getURL();
	}

	public InputStream getResourceAsStream()
	{
		return null;
	}
}