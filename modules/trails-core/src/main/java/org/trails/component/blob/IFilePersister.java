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
	IAsset getAsset(IPropertyDescriptor propertyDescriptor, Object model);

	void store(IPropertyDescriptor propertyDescriptor, Object model, IUploadFile file);

	public byte[] getData(IPropertyDescriptor propertyDescriptor, Object model);

	String getContentType(IPropertyDescriptor propertyDescriptor, Object model);

	String getFileName(IPropertyDescriptor propertyDescriptor, Object model);

	void delete(IPropertyDescriptor propertyDescriptor, Object model);
}
