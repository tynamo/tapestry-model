package org.tynamo.blob;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.tynamo.descriptor.TynamoPropertyDescriptor;


/**
 * An interface to the persistence mechanism that allows to plug in different strategies for persisting binary files.
 */
public interface IFilePersister
{
	Asset getAsset(TynamoPropertyDescriptor TynamoPropertyDescriptor, Object model);

	void store(TynamoPropertyDescriptor TynamoPropertyDescriptor, Object model, UploadedFile file);

	public byte[] getData(TynamoPropertyDescriptor TynamoPropertyDescriptor, Object model);

	String getContentType(TynamoPropertyDescriptor TynamoPropertyDescriptor, Object model);

	String getFileName(TynamoPropertyDescriptor TynamoPropertyDescriptor, Object model);

	void delete(TynamoPropertyDescriptor TynamoPropertyDescriptor, Object model);
}
