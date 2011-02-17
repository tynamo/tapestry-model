package org.tynamo.descriptor.decorators;

import ognl.Ognl;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.descriptor.*;
import org.tynamo.descriptor.annotation.handlers.DescriptorAnnotationHandler;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This class uses the Tynamo's annotations on a given class or property to modify its
 * descriptor
 */
public class TynamoDecorator implements DescriptorDecorator
{

	private static final Logger LOGGER = LoggerFactory.getLogger(TynamoDecorator.class);
	private ObjectLocator locator;

	public TynamoDecorator(ObjectLocator locator)
	{
		this.locator = locator;
	}

	public TynamoClassDescriptor decorate(TynamoClassDescriptor descriptor)
	{

		Annotation[] classAnnotations = descriptor.getBeanType().getAnnotations();
		TynamoClassDescriptor decoratedDescriptor = (TynamoClassDescriptor) decorateFromAnnotations(descriptor, classAnnotations);

		decoratedDescriptor.setPropertyDescriptors(decoratePropertyDescriptors(descriptor));
		decoratedDescriptor.setMethodDescriptors(decorateMethodDescriptors(descriptor));

		return decoratedDescriptor;
	}

	private List<TynamoPropertyDescriptor> decoratePropertyDescriptors(TynamoClassDescriptor descriptor)
	{
		List<TynamoPropertyDescriptor> decoratedPropertyDescriptors = new ArrayList<TynamoPropertyDescriptor>();
		for (TynamoPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors())
		{
			TynamoPropertyDescriptor clonedDescriptor = decoratePropertyDescriptor(propertyDescriptor);
			// recursively decorate components
			if (clonedDescriptor.isEmbedded())
			{
				clonedDescriptor = (EmbeddedDescriptor) decorate((EmbeddedDescriptor) clonedDescriptor);
			}
			decoratedPropertyDescriptors.add(clonedDescriptor);
		}
		return decoratedPropertyDescriptors;
	}

	private List<IMethodDescriptor> decorateMethodDescriptors(TynamoClassDescriptor descriptor)
	{
		List<IMethodDescriptor> decoratedMethodDescriptors = new ArrayList<IMethodDescriptor>();
		for (IMethodDescriptor methodDescriptor : descriptor.getMethodDescriptors())
		{
			IMethodDescriptor clonedDescriptor = decorateMethodDescriptor(methodDescriptor);
			decoratedMethodDescriptors.add(clonedDescriptor);
		}
		return decoratedMethodDescriptors;
	}

	protected TynamoPropertyDescriptor decoratePropertyDescriptor(TynamoPropertyDescriptor propertyDescriptor)
	{
		TynamoPropertyDescriptor clonedDescriptor = (TynamoPropertyDescriptor) propertyDescriptor.clone();
		try
		{
			Field propertyField = clonedDescriptor.getBeanType().getDeclaredField(propertyDescriptor.getName());
			clonedDescriptor = (TynamoPropertyDescriptor) decorateFromAnnotations(clonedDescriptor, propertyField.getAnnotations());

		} catch (Exception ex)
		{
			// don't care
		}
		try
		{
			PropertyDescriptor beanPropDescriptor = (PropertyDescriptor) Ognl.getValue("propertyDescriptors.{? name == '" + propertyDescriptor.getName() + "'}[0]",
					Introspector.getBeanInfo(clonedDescriptor.getBeanType()));

			Method readMethod = beanPropDescriptor.getReadMethod();
			clonedDescriptor = (TynamoPropertyDescriptor) decorateFromAnnotations(clonedDescriptor, readMethod.getAnnotations());
		}
		catch (Exception ex)
		{
			//System.out.println(propertyDescriptor.getName());
			//ex.printStackTrace();
			// don't care
		}
		return clonedDescriptor;
	}

	protected IMethodDescriptor decorateMethodDescriptor(IMethodDescriptor methodDescriptor)
	{
		try
		{

			return (IMethodDescriptor) decorateFromAnnotations(methodDescriptor, methodDescriptor.getMethod().getAnnotations());

		} catch (NoSuchMethodException nsme)
		{
			LOGGER.warn(ExceptionUtils.getRootCauseMessage(nsme));
		}
		return methodDescriptor;
	}

	private Descriptor decorateFromAnnotations(Descriptor descriptor, Annotation[] annotations)
	{
		Descriptor clonedDescriptor = (Descriptor) descriptor.clone();
		for (Annotation annotation : annotations)
		{
			// If the annotation type itself has a @HandledBy annotation, it's one of ours
			HandledBy handledBy = annotation.annotationType().getAnnotation(HandledBy.class);
			if (handledBy != null)
			{
				try
				{
					String serviceId = handledBy.value();
					DescriptorAnnotationHandler handler = locator.getService(serviceId, DescriptorAnnotationHandler.class);
					clonedDescriptor = handler.decorateFromAnnotation(annotation, clonedDescriptor);
				}
				catch (Exception ex)
				{
					LOGGER.warn(ExceptionUtils.getRootCauseMessage(ex));
				}
			}
		}
		return clonedDescriptor;
	}

}
