package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.descriptor.IMethodDescriptor;
import org.tynamo.descriptor.annotation.MethodDescriptor;


public class MethodDescriptorAnnotationHandler implements DescriptorAnnotationHandler<MethodDescriptor, IMethodDescriptor>
{

	public void decorateFromAnnotation(MethodDescriptor annotation, IMethodDescriptor descriptor)
	{
		AnnotationHandlerUtils.setPropertiesFromAnnotation(annotation, descriptor);
	}

}
