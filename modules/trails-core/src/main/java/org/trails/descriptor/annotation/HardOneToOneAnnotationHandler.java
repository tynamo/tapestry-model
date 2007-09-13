package org.trails.descriptor.annotation;

import org.trails.descriptor.IPropertyDescriptor;

public class HardOneToOneAnnotationHandler extends AbstractAnnotationHandler implements
		DescriptorAnnotationHandler<HardOneToOne, IPropertyDescriptor>
{

	public HardOneToOneAnnotationHandler()
	{
		super();
	}

	public IPropertyDescriptor decorateFromAnnotation(HardOneToOne anno, IPropertyDescriptor descriptor)
	{
		//BlobDescriptorExtension blobDescriptor = new BlobDescriptorExtension(descriptor.getPropertyType(), descriptor);
		//setPropertiesFromAnnotation(anno, blobDescriptor);
		//descriptor.addExtension(BlobDescriptorExtension.class.getName(), blobDescriptor);
		return descriptor;
	}
}
