package org.trails.component.blob;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.Parameter;
import org.trails.descriptor.IPropertyDescriptor;

/**
 *
 * @author kenneth.colassi
 *
 */
public abstract class BlobComponent extends BaseComponent {
	@Parameter(required = true, cache = true)
	public abstract Object getModel();
	public abstract void setModel(Object model);

	@Parameter(required = true)
	public abstract IPropertyDescriptor getPropertyDescriptor();
	public abstract void setPropertyDescriptor(
			IPropertyDescriptor propertyDescriptor);

	@Parameter(required = false)
	public abstract byte[] getBytes();
	public abstract void setBytes(byte[] bytes);
}