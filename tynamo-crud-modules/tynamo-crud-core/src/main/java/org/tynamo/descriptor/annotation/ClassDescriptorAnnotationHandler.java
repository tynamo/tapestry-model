package org.tynamo.descriptor.annotation;

import org.tynamo.descriptor.TrailsClassDescriptor;

public class ClassDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<ClassDescriptor, TrailsClassDescriptor>
{

	public ClassDescriptorAnnotationHandler()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public TrailsClassDescriptor decorateFromAnnotation(ClassDescriptor annotation, TrailsClassDescriptor descriptor)
	{
		/**
		 * !! This is how we get our properties migrated from our
		 * annotation to our property descriptor !! 	 
		 */
		setPropertiesFromAnnotation(annotation, descriptor);
		return descriptor;
	}


}
