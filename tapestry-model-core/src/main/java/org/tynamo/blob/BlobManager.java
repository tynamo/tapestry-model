package org.tynamo.blob;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.tynamo.descriptor.TynamoPropertyDescriptor;


/**
 * An interface to the persistence mechanism that allows to plug in different strategies for persisting binary files.
 */
public interface BlobManager
{
	Link createBlobLink(TynamoPropertyDescriptor propertyDescriptor, Object model);

	void store(TynamoPropertyDescriptor propertyDescriptor, Object model, UploadedFile file);

	public byte[] getData(TynamoPropertyDescriptor propertyDescriptor, Object model);

	String getContentType(TynamoPropertyDescriptor propertyDescriptor, Object model);

	String getFileName(TynamoPropertyDescriptor propertyDescriptor, Object model);

	void delete(TynamoPropertyDescriptor propertyDescriptor, Object model);

	boolean isNotNull(TynamoPropertyDescriptor propertyDescriptor, Object model);
}
