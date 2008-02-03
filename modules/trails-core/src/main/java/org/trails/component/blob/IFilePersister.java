package org.trails.component.blob;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.request.IUploadFile;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

/**
 * An interface to the persistence mechanism that allows to plug in different strategies for persisting binary files.
 */
public interface IFilePersister
{
	IAsset getAsset(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model);

	void store(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model,
			   IUploadFile file);

	public byte[] getData(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model);

	String getContentType(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model);

	String getFileName(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model);

	void delete(IClassDescriptor classDescriptor, IPropertyDescriptor propertyDescriptor, Object model);
}
