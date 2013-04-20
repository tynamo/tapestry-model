package org.tynamo.descriptor.decorators;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.descriptor.*;
import org.tynamo.descriptor.annotation.handlers.DescriptorAnnotationHandler;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.beans.IntrospectionException;
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
		TynamoClassDescriptor decoratedDescriptor = (TynamoClassDescriptor) descriptor.clone();
		decorateFromAnnotations(decoratedDescriptor, classAnnotations);

		decoratedDescriptor.setPropertyDescriptors(decoratePropertyDescriptors(descriptor));
		decoratedDescriptor.setMethodDescriptors(decorateMethodDescriptors(descriptor));

		return decoratedDescriptor;
	}

	List<TynamoPropertyDescriptor> decoratePropertyDescriptors(TynamoClassDescriptor descriptor)
	{
		List<TynamoPropertyDescriptor> decoratedPropertyDescriptors = new ArrayList<TynamoPropertyDescriptor>();
		for (TynamoPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors())
		{
			TynamoPropertyDescriptor clonedDescriptor = (TynamoPropertyDescriptor) propertyDescriptor.clone();
			decoratePropertyDescriptor(clonedDescriptor);

			// recursively decorate components
			if (clonedDescriptor.isEmbedded())
			{
				EmbeddedDescriptor embeddedDescriptor = (EmbeddedDescriptor) clonedDescriptor;
				embeddedDescriptor.setEmbeddedClassDescriptor(decorate(embeddedDescriptor.getEmbeddedClassDescriptor()));
			}

			decoratedPropertyDescriptors.add(clonedDescriptor);
		}
		return decoratedPropertyDescriptors;
	}

	List<IMethodDescriptor> decorateMethodDescriptors(TynamoClassDescriptor descriptor)
	{
		List<IMethodDescriptor> decoratedMethodDescriptors = new ArrayList<IMethodDescriptor>();
		for (IMethodDescriptor methodDescriptor : descriptor.getMethodDescriptors())
		{
			IMethodDescriptor clonedDescriptor = (IMethodDescriptor) methodDescriptor.clone();
			decorateMethodDescriptor(clonedDescriptor);
			decoratedMethodDescriptors.add(clonedDescriptor);
		}
		return decoratedMethodDescriptors;
	}

	void decoratePropertyDescriptor(final TynamoPropertyDescriptor descriptor)
	{

		decorateFromDeclaredField(descriptor);
		decorateFromReadMethod(descriptor);
	}

	void decorateFromReadMethod(final TynamoPropertyDescriptor tynamoPropertyDescriptor)
	{
		try
		{
			PropertyDescriptor[] descriptors =
					Introspector.getBeanInfo(tynamoPropertyDescriptor.getBeanType()).getPropertyDescriptors();

			PropertyDescriptor beanPropDescriptor = F.flow(descriptors).filter(new Predicate<PropertyDescriptor>()
			{
				public boolean accept(PropertyDescriptor descriptor)
				{
					return descriptor.getName().equals(tynamoPropertyDescriptor.getName());
				}
			}).first();

			Method readMethod = beanPropDescriptor.getReadMethod();

			if (readMethod != null)
			{
				decorateFromAnnotations(tynamoPropertyDescriptor, readMethod.getAnnotations());
			} else {
				LOGGER.error("There is no readMethod for: " + tynamoPropertyDescriptor.getBeanType().getSimpleName() + "." + tynamoPropertyDescriptor.getName());
			}
		}
		catch (IntrospectionException ex)
		{
			LOGGER.error("Could not decorate from readMethod: " + tynamoPropertyDescriptor.getBeanType().getSimpleName() + "." + tynamoPropertyDescriptor.getName());
		}
	}

	void decorateFromDeclaredField(TynamoPropertyDescriptor tynamoPropertyDescriptor)
	{
		try
		{
			Field propertyField = tynamoPropertyDescriptor.getBeanType().getDeclaredField(tynamoPropertyDescriptor.getName());
			decorateFromAnnotations(tynamoPropertyDescriptor, propertyField.getAnnotations());

		} catch (NoSuchFieldException ex)
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("NoSuchFieldException: " + tynamoPropertyDescriptor.getBeanType().getSimpleName() + "." + tynamoPropertyDescriptor.getName());
			}
		}
	}

	void decorateMethodDescriptor(IMethodDescriptor methodDescriptor)
	{
		try
		{

			decorateFromAnnotations(methodDescriptor, methodDescriptor.getMethod().getAnnotations());

		} catch (NoSuchMethodException nsme)
		{
			LOGGER.warn(ExceptionUtils.getRootCauseMessage(nsme));
		}
	}

	void decorateFromAnnotations(Descriptor descriptor, Annotation[] annotations)
	{
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
					handler.decorateFromAnnotation(annotation, descriptor);
				}
				catch (Exception ex)
				{
					LOGGER.error("error decorating: " + descriptor.toString() + " with " + annotation.toString(), ExceptionUtils.getRootCauseMessage(ex));
				}
			}
		}
	}

}
