package org.tynamo.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.tynamo.blob.BlobManager;
import org.tynamo.descriptor.TynamoPropertyDescriptor;

public class Upload
{
	@Inject
	private BlobManager filePersister;

	@Parameter(required = true)
	private Object model;

	@Parameter(required = true)
	private TynamoPropertyDescriptor propertyDescriptor;

	public UploadedFile getFile()
	{
		return null;
	}

	public void setFile(UploadedFile file)
	{
		if (file != null)
		{
			filePersister.store(propertyDescriptor, model, file);
		}
	}
}
