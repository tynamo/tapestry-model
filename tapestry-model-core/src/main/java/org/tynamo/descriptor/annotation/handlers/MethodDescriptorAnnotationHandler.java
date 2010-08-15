package org.tynamo.descriptor.annotation.handlers;

import org.tynamo.descriptor.IMethodDescriptor;
import org.tynamo.descriptor.annotation.MethodDescriptor;


public class MethodDescriptorAnnotationHandler extends AbstractAnnotationHandler
		implements DescriptorAnnotationHandler<MethodDescriptor, IMethodDescriptor>
{

	public IMethodDescriptor decorateFromAnnotation(MethodDescriptor annotation, IMethodDescriptor descriptor)
	{
		setPropertiesFromAnnotation(annotation, descriptor);
		return descriptor;
	}
}
