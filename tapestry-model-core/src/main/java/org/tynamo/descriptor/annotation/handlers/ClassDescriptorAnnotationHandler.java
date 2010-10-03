package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.ClassDescriptor;

public class ClassDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<ClassDescriptor, TynamoClassDescriptor>
{

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
