package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.PageType;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.annotation.BlobDescriptor;
import org.tynamo.descriptor.extension.BeanModelExtension;
import org.tynamo.descriptor.extension.BlobDescriptorExtension;

public class BlobDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<BlobDescriptor, TynamoPropertyDescriptor>
{

	public BlobDescriptorAnnotationHandler()
	{
		super();
	}

	public TynamoPropertyDescriptor decorateFromAnnotation(BlobDescriptor propertyDescriptorAnno,
	                                                       TynamoPropertyDescriptor descriptor)
	{
		BlobDescriptorExtension blobDescriptor = new BlobDescriptorExtension(descriptor.getPropertyType());
		setPropertiesFromAnnotation(propertyDescriptorAnno, blobDescriptor);
		descriptor.addExtension(BlobDescriptorExtension.class, blobDescriptor);

		BeanModelExtension beanModelExtension = BeanModelExtension.obtainBeanModelExtension(descriptor);

		beanModelExtension.addToExcludeMap(PageType.LIST.getContextKey(), descriptor.getName());
		return descriptor;
	}


}
