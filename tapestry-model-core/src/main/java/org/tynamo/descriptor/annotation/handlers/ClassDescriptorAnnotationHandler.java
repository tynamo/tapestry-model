package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.annotation.ClassDescriptor;

public class ClassDescriptorAnnotationHandler implements DescriptorAnnotationHandler<ClassDescriptor, TynamoClassDescriptor>
{

	public void decorateFromAnnotation(ClassDescriptor annotation, TynamoClassDescriptor descriptor)
	{
		/**
		 * !! This is how we get our properties migrated from our
		 * annotation to our property descriptor !!
		 */
		AnnotationHandlerUtils.setPropertiesFromAnnotation(annotation, descriptor);
	}


}
