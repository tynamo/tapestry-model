package org.trails.component.blob;

import org.apache.tapestry.asset.AbstractAsset;
import org.apache.tapestry.engine.ILink;

import java.io.InputStream;

public class TrailsBlobAsset extends AbstractAsset
{

	private BlobDownloadService bytesService;

	private String idProperty;

	private String entityName;

	private String bytesProperty;

	public TrailsBlobAsset(BlobDownloadService chartService, String entityName, String idProperty, String bytesProperty)
	{
		super(null, null);
		this.bytesService = chartService;
		this.entityName = entityName;
		this.idProperty = idProperty;
		this.bytesProperty = bytesProperty;
	}

	public String getBytesProperty()
	{
		return bytesProperty;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public String getIdProperty()
	{
		return idProperty;
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