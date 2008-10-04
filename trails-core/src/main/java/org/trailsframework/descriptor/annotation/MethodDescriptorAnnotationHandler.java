package org.trails.descriptor.annotation;

import org.trails.descriptor.IMethodDescriptor;


public class MethodDescriptorAnnotationHandler extends AbstractAnnotationHandler
		implements DescriptorAnnotationHandler<MethodDescriptor, IMethodDescriptor>
{

	public IMethodDescriptor decorateFromAnnotation(MethodDescriptor annotation, IMethodDescriptor descriptor)
	{
		setPropertiesFromAnnotation(annotation, descriptor);
		return descriptor;
	}
}
