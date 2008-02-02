package org.trails.component.blob;

import org.apache.commons.io.IOUtils;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.asset.ExternalAsset;
import org.apache.tapestry.request.IUploadFile;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.extension.BlobDescriptorExtension;
import org.trails.descriptor.extension.ITrailsBlob;
import org.trails.persistence.PersistenceService;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;


public class DefaultFilePersister implements IFilePersister
{

	PersistenceService persistenceService;
	BlobDownloadService blobDownloadService;


	public void store(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model,
					  IUploadFile file)
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
				PropertyUtils.write(model, propertyDescriptor.getName(), data);
			} else if (blobDescriptorExtension.isITrailsBlob())
			{
				ITrailsBlob trailsBlob = (ITrailsBlob) PropertyUtils.read(model, propertyDescriptor.getName());
				trailsBlob.setFileName(file.getFileName());
				trailsBlob.setFilePath(file.getFilePath());
				trailsBlob.setContentType(file.getContentType());
				trailsBlob.setBytes(data);
				//@todo check for NPE
			}
		}
	}

	public IAsset getAsset(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model)
	{
		Serializable pk = persistenceService.getIdentifier(model, classDescriptor);
		if (pk != null)
		{
			BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);
			String id = pk.toString();

			if (blobDescriptorExtension.isBytes())
			{
				return new TrailsBlobAsset(blobDownloadService, classDescriptor.getType().getName(), id,
						propertyDescriptor.getName(), blobDescriptorExtension.getContentType(),
						blobDescriptorExtension.getFileName());

			} else if (blobDescriptorExtension.isITrailsBlob())
			{
				ITrailsBlob trailsBlob = (ITrailsBlob) PropertyUtils.read(model, propertyDescriptor.getName());
				return new TrailsBlobAsset(blobDownloadService, classDescriptor.getType().getName(), id,
						propertyDescriptor.getName(), trailsBlob.getContentType(), trailsBlob.getFileName());
			}
		}

		return new ExternalAsset("http://alpha.amneris.es/speeddial/no-image.gif", null);
	}

	public String getContentType(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);
		if (blobDescriptorExtension.isITrailsBlob())
		{
			ITrailsBlob trailsBlob = (ITrailsBlob) PropertyUtils.read(model, propertyDescriptor.getName());
			if (trailsBlob != null)
			{
				return trailsBlob.getContentType();
			}
		}
		return blobDescriptorExtension.getContentType();
	}

	public String getFileName(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);
		if (blobDescriptorExtension.isITrailsBlob())
		{
			ITrailsBlob trailsBlob = (ITrailsBlob) PropertyUtils.read(model, propertyDescriptor.getName());
			if (trailsBlob != null)
			{
				return trailsBlob.getFileName();
			}
		}
		return blobDescriptorExtension.getFileName();
	}

	private BlobDescriptorExtension getBlobDescriptorExtension(IPropertyDescriptor propertyDescriptor)
	{
		return propertyDescriptor.getExtension(BlobDescriptorExtension.class);
	}

	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public void setBlobDownloadService(BlobDownloadService blobDownloadService)
	{
		this.blobDownloadService = blobDownloadService;
	}
}
