package org.trails.descriptor.annotation;

import org.trails.descriptor.IClassDescriptor;

public class ClassDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<ClassDescriptor, IClassDescriptor>
{

	public ClassDescriptorAnnotationHandler()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public IClassDescriptor decorateFromAnnotation(ClassDescriptor annotation, IClassDescriptor descriptor)
	{
		/**
		 * !! This is how we get our properties migrated from our
		 * annotation to our property descriptor !! 	 
		 */
		setPropertiesFromAnnotation(annotation, descriptor);
		return descriptor;
	}


}
