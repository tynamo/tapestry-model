package org.tynamo.descriptor.annotation;

import ognl.Ognl;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.descriptor.*;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class uses the annotations on a given class or property to modify its
 * descriptor
 *
 * @author Chris Nelson
 */
public class AnnotationDecorator implements DescriptorDecorator
{

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationDecorator.class);

	public TrailsClassDescriptor decorate(TrailsClassDescriptor descriptor)
	{

		Annotation[] classAnnotations = descriptor.getType().getAnnotations();
		TrailsClassDescriptor decoratedDescriptor = (TrailsClassDescriptor) decorateFromAnnotations(descriptor, classAnnotations);

		decoratedDescriptor.setPropertyDescriptors(decoratePropertyDescriptors(descriptor));
		sortDescriptors(decoratedDescriptor.getPropertyDescriptors());

		decoratedDescriptor.setMethodDescriptors(decorateMethodDescriptors(descriptor));

		return decoratedDescriptor;
	}

	private List<TrailsPropertyDescriptor> decoratePropertyDescriptors(TrailsClassDescriptor descriptor)
	{
		List<TrailsPropertyDescriptor> decoratedPropertyDescriptors = new ArrayList<TrailsPropertyDescriptor>();
		for (TrailsPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors())
		{
			TrailsPropertyDescriptor clonedDescriptor = decoratePropertyDescriptor(propertyDescriptor);
			// recursively decorate components
			if (clonedDescriptor.isEmbedded())
			{
				clonedDescriptor = (EmbeddedDescriptor) decorate((EmbeddedDescriptor) clonedDescriptor);
			}
			decoratedPropertyDescriptors.add(clonedDescriptor);
		}
		return decoratedPropertyDescriptors;
	}

	private List<IMethodDescriptor> decorateMethodDescriptors(TrailsClassDescriptor descriptor)
	{
		List<IMethodDescriptor> decoratedMethodDescriptors = new ArrayList<IMethodDescriptor>();
		for (IMethodDescriptor methodDescriptor : descriptor.getMethodDescriptors())
		{
			IMethodDescriptor clonedDescriptor = decorateMethodDescriptor(methodDescriptor);
			decoratedMethodDescriptors.add(clonedDescriptor);
		}
		return decoratedMethodDescriptors;
	}

	protected TrailsPropertyDescriptor decoratePropertyDescriptor(TrailsPropertyDescriptor propertyDescriptor)
	{
		TrailsPropertyDescriptor clonedDescriptor = (TrailsPropertyDescriptor) propertyDescriptor.clone();
		try
		{
			Field propertyField = clonedDescriptor.getBeanType().getDeclaredField(propertyDescriptor.getName());
			clonedDescriptor = (TrailsPropertyDescriptor) decorateFromAnnotations(clonedDescriptor, propertyField.getAnnotations());

		} catch (Exception ex)
		{
			// don't care
		}
		try
		{
			PropertyDescriptor beanPropDescriptor = (PropertyDescriptor) Ognl.getValue("propertyDescriptors.{? name == '" + propertyDescriptor.getName() + "'}[0]",
					Introspector.getBeanInfo(clonedDescriptor.getBeanType()));

			Method readMethod = beanPropDescriptor.getReadMethod();
			clonedDescriptor = (TrailsPropertyDescriptor) decorateFromAnnotations(clonedDescriptor, readMethod.getAnnotations());
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

	/**
	 * Rearrange the property descriptors by their index
	 *
	 * @param propertyDescriptors
	 */
	private void sortDescriptors(List<TrailsPropertyDescriptor> propertyDescriptors)
	{
		for (TrailsPropertyDescriptor propertyDescriptor : Collections.unmodifiableList(propertyDescriptors))
		{
			if (propertyDescriptor.getIndex() != TrailsPropertyDescriptor.UNDEFINED_INDEX)
			{
				Collections.swap(propertyDescriptors, propertyDescriptor.getIndex(),
						propertyDescriptors.indexOf(propertyDescriptor));
			}
		}
	}

	private Descriptor decorateFromAnnotations(Descriptor descriptor, Annotation[] annotations)
	{
		Descriptor clonedDescriptor = (Descriptor) descriptor.clone();
		for (Annotation annotation : annotations)
		{
			// If the annotation type itself has a DescriptorAnnotation, it's one of ours
			DescriptorAnnotation handlerAnnotation =
					annotation.annotationType().getAnnotation(DescriptorAnnotation.class);
			if (handlerAnnotation != null)
			{
				try
				{
					DescriptorAnnotationHandler handler = handlerAnnotation.value().newInstance();
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
