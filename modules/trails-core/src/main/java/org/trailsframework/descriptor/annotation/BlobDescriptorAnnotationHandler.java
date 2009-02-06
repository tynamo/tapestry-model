package org.trailsframework.descriptor.annotation;

import org.trailsframework.descriptor.extension.BlobDescriptorExtension;
import org.trailsframework.descriptor.TrailsPropertyDescriptor;

public class BlobDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<BlobDescriptor, TrailsPropertyDescriptor>
{

	public BlobDescriptorAnnotationHandler()
	{
		super();
	}

	public TrailsPropertyDescriptor decorateFromAnnotation(BlobDescriptor propertyDescriptorAnno, TrailsPropertyDescriptor descriptor)
	{
		BlobDescriptorExtension blobDescriptor = new BlobDescriptorExtension(descriptor.getPropertyType());
		setPropertiesFromAnnotation(propertyDescriptorAnno, blobDescriptor);
		descriptor.addExtension(BlobDescriptorExtension.class.getName(), blobDescriptor);
		return descriptor;
	}


}
