package org.tynamo.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;
import org.tynamo.blob.BlobManager;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.extension.BlobDescriptorExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Download
{
	private final static String noIcon = "/images/cross.png";
	private final static String noImage = "/images/noimage.jpg";

	@Inject
	private BlobManager filePersister;

/*
	@Inject
	private IconResolver iconResolver;

	@Inject
	private AssetFactory contextAssetFactory;
*/

	@Inject
	private ComponentResources resources;

	@Parameter(required = true)
	private Object model;

	@Parameter(required = true)
	private TynamoPropertyDescriptor propertyDescriptor;

	public BlobDescriptorExtension.RenderType getRenderType()
	{
		return propertyDescriptor.getExtension(BlobDescriptorExtension.class).getRenderType();
	}

	public StreamResponse onByteArrayStream()
	{
		return new StreamResponse()
		{
			public String getContentType()
			{
				return filePersister.getContentType(propertyDescriptor, model);
			}

			public InputStream getStream() throws IOException
			{
				return new ByteArrayInputStream(filePersister.getData(propertyDescriptor, model));
			}

			public void prepareResponse(Response response)
			{
			}
		};
	}

	public Link getByteArrayLink()
	{
		return resources.createEventLink("byteArrayStream", false, new Object[]{});
	}

/*
	public Asset getIcon()
	{
		return getContentType() != null ? iconResolver.getAsset(getContentType()) : getNoIcon();
	}
*/

	private String getContentType()
	{
		return filePersister.getContentType(propertyDescriptor, model);
	}

	public String getFileName()
	{
		return filePersister.getFileName(propertyDescriptor, model);
	}

	public Asset getNoIcon()
	{
//		return contextAssetFactory.createAbsoluteAsset(noIcon, null, null);
		return null;
	}

	public Asset getNoImage()
	{
//		return contextAssetFactory.createAbsoluteAsset(noImage, null, null);
		return null;
	}
}
