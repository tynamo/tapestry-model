package org.tynamo.blob;

import org.apache.tapestry5.internal.services.AbstractAsset;
import org.apache.tapestry5.ioc.Resource;
import org.tynamo.descriptor.TynamoPropertyDescriptor;

import java.io.InputStream;
import java.io.Serializable;

/**
 * An implementation of {@link AbstractAsset} for assets that are entity properties.
 */
public class BlobAsset extends AbstractAsset
{

	private BlobDownloadService bytesService;

	private Serializable id;

	private TynamoPropertyDescriptor propertyDescriptor;

	public BlobAsset(boolean invariant)
	{
		super(invariant);
	}

	public BlobAsset(BlobDownloadService chartService, TynamoPropertyDescriptor propertyDescriptor,
					 Serializable id)
	{
		super(false);
		this.bytesService = chartService;
		this.id = id;
		this.propertyDescriptor = propertyDescriptor;
	}

	public TynamoPropertyDescriptor getPropertyDescriptor()
	{
		return propertyDescriptor;
	}

	public Serializable getId()
	{
		return id;
	}

	public String buildURL()
	{
/*
		Link l = bytesService.getLink(false, new Object[]{this});
		return l.toAbsoluteURI();
*/
		return null;
	}

	public InputStream getResourceAsStream()
	{
		return null;
	}

	public String toClientURL()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public Resource getResource()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}