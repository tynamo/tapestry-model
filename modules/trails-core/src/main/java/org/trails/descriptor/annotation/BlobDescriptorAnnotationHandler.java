package org.trails.descriptor.annotation;

import org.trails.descriptor.extension.BlobDescriptorExtension;
import org.trails.descriptor.IPropertyDescriptor;

public class BlobDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<BlobDescriptor, IPropertyDescriptor>
{

	public BlobDescriptorAnnotationHandler()
	{
		super();
	}

	public IPropertyDescriptor decorateFromAnnotation(BlobDescriptor propertyDescriptorAnno, IPropertyDescriptor descriptor)
	{
		BlobDescriptorExtension blobDescriptor = new BlobDescriptorExtension(descriptor.getPropertyType());
		setPropertiesFromAnnotation(propertyDescriptorAnno, blobDescriptor);
		descriptor.addExtension(BlobDescriptorExtension.class.getName(), blobDescriptor);
		return descriptor;
	}


}
