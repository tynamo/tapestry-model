package org.tynamo.blob;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.tynamo.builder.BuilderDirector;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.extension.BlobDescriptorExtension;
import org.tynamo.descriptor.extension.TynamoBlob;
import org.tynamo.services.PersistenceService;

import java.io.IOException;
import java.io.InputStream;


/**
 * The Tynamo {@link BlobManager} default implementation.
 */
public class BlobManagerImpl implements BlobManager
{

	private PersistenceService persistenceService;
	private PropertyAccess propertyAccess;
	private PageRenderLinkSource pageRenderLinkSource;
	private BuilderDirector builderDirector;

	public BlobManagerImpl(PersistenceService persistenceService, PropertyAccess propertyAccess,
	                       PageRenderLinkSource pageRenderLinkSource, BuilderDirector builderDirector)
	{
		this.persistenceService = persistenceService;
		this.propertyAccess = propertyAccess;
		this.pageRenderLinkSource = pageRenderLinkSource;
		this.builderDirector = builderDirector;
	}

	public void store(TynamoPropertyDescriptor propertyDescriptor, Object model, UploadedFile file)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		byte[] data = new byte[0];
		InputStream inputStream = file.getStream();

		try
		{
			data = IOUtils.toByteArray(inputStream);

		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		}

		if (data.length > 1)
		{
			if (blobDescriptorExtension.isBytes())
			{
				propertyAccess.set(model, propertyDescriptor.getName(), data);
			} else if (blobDescriptorExtension.isITynamoBlob())
			{
				TynamoBlob tynamoBlob = (TynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());

				if (tynamoBlob == null)
				{
					//trying to avoid an NPE
					tynamoBlob = (TynamoBlob) builderDirector.createNewInstance(propertyDescriptor.getPropertyType());
					propertyAccess.set(model, propertyDescriptor.getName(), tynamoBlob);
				}

				tynamoBlob.setFileName(file.getFileName());
				tynamoBlob.setFilePath(file.getFilePath());
				tynamoBlob.setContentType(file.getContentType());
				tynamoBlob.setBytes(data);
			}
		}
	}

	public byte[] getData(TynamoPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		if (blobDescriptorExtension.isBytes())
		{
			return (byte[]) propertyAccess.get(model, propertyDescriptor.getName());

		} else if (blobDescriptorExtension.isITynamoBlob())
		{
			TynamoBlob tynamoBlob = (TynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());
			return tynamoBlob != null ? tynamoBlob.getBytes() : new byte[0];
		}
		return null;
	}

	public void delete(TynamoPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		if (blobDescriptorExtension.isBytes())
		{
			propertyAccess.set(model, propertyDescriptor.getName(), new byte[0]);

		} else if (blobDescriptorExtension.isITynamoBlob())
		{
			TynamoBlob tynamoBlob = (TynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());
			tynamoBlob.reset();
		}

		persistenceService.save(model);
	}

	public Link createBlobLink(TynamoPropertyDescriptor propertyDescriptor, Object model)
	{
		return pageRenderLinkSource
				.createPageRenderLinkWithContext("tynamo/Blob", propertyDescriptor.getBeanType(), model, propertyDescriptor.getName());
	}

	public String getContentType(TynamoPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		if (blobDescriptorExtension.isITynamoBlob())
		{
			TynamoBlob tynamoBlob = (TynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());
			if (tynamoBlob != null)
			{
				return tynamoBlob.getContentType();
			}
		}

		return blobDescriptorExtension.getContentType();
	}

	public String getFileName(TynamoPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		if (StringUtils.isNotEmpty(blobDescriptorExtension.getFileName()))
		{
			return blobDescriptorExtension.getFileName();
		}

		if (blobDescriptorExtension.isITynamoBlob())
		{
			TynamoBlob tynamoBlob = (TynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());
			if (tynamoBlob != null)
			{
				return tynamoBlob.getFileName();
			}
		}

		return null;
	}

	public boolean isNotNull(TynamoPropertyDescriptor propertyDescriptor, Object model)
	{
		if (model != null)
		{
			BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);
			byte[] bytes = null;
			if (blobDescriptorExtension.isITynamoBlob())
			{
				TynamoBlob tynamoBlob = (TynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());
				bytes = tynamoBlob != null ? tynamoBlob.getBytes() : null;
			} else if (blobDescriptorExtension.isBytes()) {
				bytes = (byte[]) propertyAccess.get(model, propertyDescriptor.getName());
			}
			return bytes != null && bytes.length > 0;
		}
		return false;
	}

	private BlobDescriptorExtension getBlobDescriptorExtension(TynamoPropertyDescriptor propertyDescriptor)
	{
		return propertyDescriptor.getExtension(BlobDescriptorExtension.class);
	}
}
