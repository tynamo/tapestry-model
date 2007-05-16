package org.trails.component.blob;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.request.IUploadFile;
import org.hibernate.LazyInitializationException;
import org.trails.descriptor.BlobDescriptorExtension;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceService;

@ComponentClass(allowBody = true, allowInformalParameters = true)
public abstract class TrailsUpload extends BaseComponent
{
	@Parameter(required = true)
	public abstract IPropertyDescriptor getDescriptor();

	public abstract void setDescriptor(
		IPropertyDescriptor descriptor);

	@Parameter(required = true)
	public abstract Object getBytes();

	public abstract void setBytes(Object bytes);

	@Parameter(required = true)
	public abstract Object getModel();

	public abstract void setModel(Object bytes);

	@InjectObject("spring:persistenceService")
	public abstract PersistenceService getPersistenceService();

	public BlobDescriptorExtension getBlobDescriptorExtension()
	{
		return getDescriptor().getExtension(
			BlobDescriptorExtension.class);
	}

	public IUploadFile getFile()
	{
		return null;
	}

	public void setFile(IUploadFile file)
	{
		if (file != null)
		{
			ITrailsBlob trailsBlob = null;
			byte[] data = new byte[0];
			InputStream inputStream = file.getStream();
			try
			{
				data = IOUtils.toByteArray(inputStream);
				if (getBlobDescriptorExtension().isBytes())
				{
					trailsBlob = (ITrailsBlob) getModel();
				} else if (getBlobDescriptorExtension().isITrailsBlob())
				{
					trailsBlob = (ITrailsBlob) getBytes();
				}
			} catch (IOException ioe)
			{
				ioe.printStackTrace();
			}

			if (data.length > 1)
			{
				try
				{
					trailsBlob.setFileName(file.getFileName());
					trailsBlob.setFilePath(file.getFilePath());
					trailsBlob.setFileExtension(file.getFilePath());
					trailsBlob.setContentType(file.getContentType());
					trailsBlob.setNumBytes(new Long(((byte[]) trailsBlob
						.getBytes()).length));
					trailsBlob.setBytes(data);
				} catch (LazyInitializationException e)
				{
					getPersistenceService().reattach(trailsBlob);

					trailsBlob.setFileName(file.getFileName());
					trailsBlob.setFilePath(file.getFilePath());
					trailsBlob.setFileExtension(file.getFilePath());
					trailsBlob.setContentType(file.getContentType());
					trailsBlob.setNumBytes(new Long(((byte[]) trailsBlob
						.getBytes()).length));
					trailsBlob.setBytes(data);
				}
			}
		}
	}
}
