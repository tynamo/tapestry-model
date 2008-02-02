package org.trails.component.blob;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IAsset;
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

	public byte[] getData(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		if (blobDescriptorExtension.isBytes())
		{
			return (byte[]) PropertyUtils.read(model, propertyDescriptor.getName());

		} else if (blobDescriptorExtension.isITrailsBlob())
		{
			ITrailsBlob trailsBlob = (ITrailsBlob) PropertyUtils.read(model, propertyDescriptor.getName());
			return trailsBlob.getBytes();
		}
		return null;
	}

	public void delete(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		if (blobDescriptorExtension.isBytes())
		{
			PropertyUtils.write(model, propertyDescriptor.getName(), new byte[0]);

		} else if (blobDescriptorExtension.isITrailsBlob())
		{
			ITrailsBlob trailsBlob = (ITrailsBlob) PropertyUtils.read(model, propertyDescriptor.getName());
			trailsBlob.reset();
		}

		persistenceService.save(model);
	}

	public IAsset getAsset(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model)
	{
		Serializable pk = persistenceService.getIdentifier(model, classDescriptor);
		if (pk != null)
		{
			String id = pk.toString();

			return new TrailsBlobAsset(blobDownloadService, classDescriptor.getType().getName(), id,
					propertyDescriptor.getName());
		}
		return null;
	}

	public String getContentType(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		if (StringUtils.isNotEmpty(blobDescriptorExtension.getFileName()))
		{
			return blobDescriptorExtension.getContentType();
		}


		if (blobDescriptorExtension.isITrailsBlob())
		{
			ITrailsBlob trailsBlob = (ITrailsBlob) PropertyUtils.read(model, propertyDescriptor.getName());
			if (trailsBlob != null)
			{
				return trailsBlob.getContentType();
			}
		}

		return null;
	}

	public String getFileName(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model)
	{
		BlobDescriptorExtension blobDescriptorExtension = getBlobDescriptorExtension(propertyDescriptor);

		if (StringUtils.isNotEmpty(blobDescriptorExtension.getFileName()))
		{
			return blobDescriptorExtension.getFileName();
		}

		if (blobDescriptorExtension.isITrailsBlob())
		{
			ITrailsBlob trailsBlob = (ITrailsBlob) PropertyUtils.read(model, propertyDescriptor.getName());
			if (trailsBlob != null)
			{
				return trailsBlob.getFileName();
			}
		}

		return null;
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
