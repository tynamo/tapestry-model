package org.trails.component.blob;

import org.apache.tapestry.asset.AbstractAsset;
import org.apache.tapestry.engine.ILink;

import java.io.InputStream;

/**
  * An implementation of {@link org.apache.tapestry.IAsset} for assets that are entity properties.
 */
public class TrailsBlobAsset extends AbstractAsset
{

	private BlobDownloadService bytesService;

	private String id;

	private String entityName;

	private String bytesProperty;

	public TrailsBlobAsset(BlobDownloadService chartService, String entityName, String id, String bytesProperty)
	{
		super(null, null);
		this.bytesService = chartService;
		this.entityName = entityName;
		this.id = id;
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

	public String getId()
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