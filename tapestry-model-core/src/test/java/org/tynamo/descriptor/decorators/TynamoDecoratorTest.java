package org.tynamo.descriptor.decorators;


import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.test.TapestryTestCase;
import org.easymock.EasyMock;
import org.testng.annotations.Test;
import org.tynamo.descriptor.TynamoClassDescriptorImpl;
import org.tynamo.descriptor.annotation.handlers.DescriptorAnnotationHandler;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TynamoDecoratorTest extends TapestryTestCase
{

	@Test
	void decorateFromAnnotations()
	{
		TynamoClassDescriptorImpl descriptor = new TynamoClassDescriptorImpl(AnnotatedClass.class);
		HandledAnnotation annotation = AnnotatedClass.class.getAnnotation(HandledAnnotation.class);

		ObjectLocator locator = newMock(ObjectLocator.class);
		DescriptorAnnotationHandler annotationHandler = newMock(DescriptorAnnotationHandler.class);

		annotationHandler.decorateFromAnnotation(annotation, descriptor);
		EasyMock.expectLastCall().once();

		expect(locator.getService("AnnotationHandler", DescriptorAnnotationHandler.class)).andReturn(annotationHandler);

		replay();

		TynamoDecorator decorator = new TynamoDecorator(locator);

		decorator.decorateFromAnnotations(descriptor, AnnotatedClass.class.getAnnotations());

		verify();

	}

}

@Retention(RetentionPolicy.RUNTIME)
@HandledBy("AnnotationHandler")
@interface HandledAnnotation {}

@HandledAnnotation
class AnnotatedClass {}