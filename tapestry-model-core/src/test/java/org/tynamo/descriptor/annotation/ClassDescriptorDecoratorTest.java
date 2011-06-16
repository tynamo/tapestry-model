package org.tynamo.descriptor.annotation;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptorImpl;
import org.tynamo.descriptor.annotation.handlers.ClassDescriptorAnnotationHandler;

public class ClassDescriptorDecoratorTest extends Assert
{

	@Test
	public void testDecorate() throws Exception
	{
		ClassDescriptorAnnotationHandler decorator = new ClassDescriptorAnnotationHandler();
		TynamoClassDescriptorImpl descriptor = new TynamoClassDescriptorImpl(Annotated.class);
		ClassDescriptor classDescriptorAnno = Annotated.class.getAnnotation(ClassDescriptor.class);
		decorator.decorateFromAnnotation(classDescriptorAnno, descriptor);
		assertTrue(descriptor.isNonVisual());
		assertTrue(descriptor.getHasCyclicRelationships());
	}
}
