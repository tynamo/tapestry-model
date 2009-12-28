package org.tynamo.descriptor.annotation;

import org.tynamo.descriptor.TynamoClassDescriptor;

public class ClassDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<ClassDescriptor, TynamoClassDescriptor>
{

	public ClassDescriptorAnnotationHandler()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public TynamoClassDescriptor decorateFromAnnotation(ClassDescriptor annotation, TynamoClassDescriptor descriptor)
	{
		/**
		 * !! This is how we get our properties migrated from our
		 * annotation to our property descriptor !! 	 
		 */
		setPropertiesFromAnnotation(annotation, descriptor);
		return descriptor;
	}


}
