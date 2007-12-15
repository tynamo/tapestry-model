package org.trails.descriptor.annotation;

import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.extension.OwningObjectReferenceDescriptor;

public class HardOneToOneAnnotationHandler extends AbstractAnnotationHandler implements
		DescriptorAnnotationHandler<HardOneToOne, IPropertyDescriptor>
{

	public HardOneToOneAnnotationHandler()
	{
		super();
	}

	public IPropertyDescriptor decorateFromAnnotation(HardOneToOne propertyDescriptorAnno,
			IPropertyDescriptor descriptorReference)
	{
		if (propertyDescriptorAnno.identity() == org.trails.descriptor.annotation.HardOneToOne.Identity.OWNER)
		{
			OwningObjectReferenceDescriptor owningObjectReferenceDescriptor = new OwningObjectReferenceDescriptor();

			Class type = descriptorReference.getPropertyType();
			String inverseProperty = descriptorReference.getName();
			owningObjectReferenceDescriptor.setInverseProperty(inverseProperty);
			setPropertiesFromAnnotation(propertyDescriptorAnno, owningObjectReferenceDescriptor);
			descriptorReference.addExtension(OwningObjectReferenceDescriptor.class.getName(),
					owningObjectReferenceDescriptor);
		}
		return descriptorReference;
	}
}
