package org.tynamo.descriptor.annotation;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptorImpl;

public class ClassDescriptorDecoratorTest extends Assert
{

	@Test
	public void testDecorate() throws Exception
	{
		ClassDescriptorAnnotationHandler decorator = new ClassDescriptorAnnotationHandler();
		TynamoClassDescriptorImpl descriptor = new TynamoClassDescriptorImpl(Annotated.class);
		ClassDescriptor classDescriptorAnno = Annotated.class.getAnnotation(ClassDescriptor.class);
		TynamoClassDescriptor decoratedDescriptor = decorator.decorateFromAnnotation(classDescriptorAnno, descriptor);
		assertTrue(decoratedDescriptor.isHidden());
		assertTrue(decoratedDescriptor.getHasCyclicRelationships());
	}
}
