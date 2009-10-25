package org.tynamo.blob;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.extension.BlobDescriptorExtension;
import org.tynamo.descriptor.extension.ITynamoBlob;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;


/**
 * The Trails {@link IFilePersister} default implementation.
 */
public class DefaultFilePersister implements IFilePersister
{

	private PersistenceService persistenceService;
	private DescriptorService descriptorService;
//7	private BlobDownloadService blobDownloadService;
	private PropertyAccess propertyAccess;

	public DefaultFilePersister(PersistenceService persistenceService, DescriptorService descriptorService,
								PropertyAccess propertyAccess)
	{
		this.persistenceService = persistenceService;
		this.descriptorService = descriptorService;
		this.propertyAccess = propertyAccess;
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
				ITynamoBlob trailsBlob = (ITynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());

				if (trailsBlob == null)
				{ //trying to avoid an NPE
					trailsBlob = new TrailsBlobImpl();
					propertyAccess.set(model, propertyDescriptor.getName(), trailsBlob);
				}

				trailsBlob.setFileName(file.getFileName());
				trailsBlob.setFilePath(file.getFilePath());
				trailsBlob.setContentType(file.getContentType());
				trailsBlob.setBytes(data);
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
			ITynamoBlob trailsBlob = (ITynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());
			return trailsBlob != null ? trailsBlob.getBytes() : new byte[0];
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
			ITynamoBlob trailsBlob = (ITynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());
			trailsBlob.reset();
		}

		persistenceService.save(model);
	}

	public Asset getAsset(TynamoPropertyDescriptor propertyDescriptor, Object model)
	{
		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(propertyDescriptor.getBeanType());
		Serializable pk = propertyAccess.get(model, classDescriptor.getIdentifierDescriptor().getName()).toString();

		if (pk != null)
		{
			byte[] bytes = getData(propertyDescriptor, model);

			if (bytes != null && bytes.length > 0)
			{
//				return new TrailsBlobAsset(blobDownloadService, propertyDescriptor, pk);
			}
		}
		return null;
	}

	public String getContentType(TynamoPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		if (StringUtils.isNotEmpty(blobDescriptorExtension.getFileName()))
		{
			return blobDescriptorExtension.getContentType();
		}


		if (blobDescriptorExtension.isITynamoBlob())
		{
			ITynamoBlob trailsBlob = (ITynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());
			if (trailsBlob != null)
			{
				return trailsBlob.getContentType();
			}
		}

		return null;
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
			ITynamoBlob trailsBlob = (ITynamoBlob) propertyAccess.get(model, propertyDescriptor.getName());
			if (trailsBlob != null)
			{
				return trailsBlob.getFileName();
			}
		}

		return null;
	}

	private BlobDescriptorExtension getBlobDescriptorExtension(TynamoPropertyDescriptor propertyDescriptor)
	{
		return propertyDescriptor.getExtension(BlobDescriptorExtension.class);
	}
}
