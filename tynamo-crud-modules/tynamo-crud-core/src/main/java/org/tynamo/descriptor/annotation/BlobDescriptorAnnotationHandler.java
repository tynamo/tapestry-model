package org.tynamo.descriptor.annotation;

import org.tynamo.descriptor.extension.BlobDescriptorExtension;
import org.tynamo.descriptor.TrailsPropertyDescriptor;

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
